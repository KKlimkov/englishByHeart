package org.example.englishByHeart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.englishByHeart.domain.*;
import org.example.englishByHeart.dto.*;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    private static final String SENTENCE_IDS_API_URL = "http://localhost:8080/api/sentence/sentencesIdsByTopicsAndRules";
    private static final String SENTENCE_BY_ID_API_URL = "http://localhost:8080/api/sentence/sentenceById";
    private static final String TRANSLATIONS_BY_SENTENCE_ID_API_URL = "http://localhost:8080/translations/translations";


    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private RestTemplate restTemplate;

    public PickedElementResponse removeRandomElement(List<Long> array) {
        if (array.isEmpty()) {
            return new PickedElementResponse(new ArrayList<>(), null); // Return an empty list and null if input array is empty
        }

        List<Long> copy = new ArrayList<>(array); // Make a copy of the original array
        Collections.shuffle(copy); // Shuffle the copy
        Long pickedElement = copy.remove(0); // Remove the first element (randomly picked)

        return new PickedElementResponse(copy, pickedElement); // Return the modified array and the picked element
    }

    public static class PickedElementResponse {
        private List<Long> modifiedArray;
        private Long pickedElement;

        public PickedElementResponse(List<Long> modifiedArray, Long pickedElement) {
            this.modifiedArray = modifiedArray;
            this.pickedElement = pickedElement;
        }

        public List<Long> getModifiedArray() {
            return modifiedArray;
        }

        public Long getPickedElement() {
            return pickedElement;
        }
    }

    public Exercise createExercise(CreateExerciseRequest request) {
        logger.info("Creating exercise with request: {}", request);

        Exercise exercise = new Exercise();
        exercise.setUserId(request.getUserId());
        exercise.setExerciseName(request.getExerciseName());
        exercise.setSentencesId(convertListToStringArray(request.getSentencesId()));
        exercise.setTopicsIds(convertListToStringArray(request.getTopicsIds()));
        exercise.setRulesIds(convertListToStringArray(request.getRulesIds()));
        exercise.setCurrentSentencesId(convertListToStringArray(request.getSentencesId()));

        Exercise savedExercise = exerciseRepository.save(exercise);

        logger.info("Saved exercise: {}", savedExercise);

        return savedExercise;
    }

    private String[] convertListToStringArray(List<Long> list) {
        if (list == null) {
            return new String[0];
        }
        return list.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    public List<Exercise> updateExercisesByUserId(Long userId, List<Long> currentSentences) {
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);
        if (exercises.isEmpty()) {
            // No exercises found for the given user ID
            return Collections.emptyList();
        }
        // Update each exercise with the new currentSentencesId
        for (Exercise exercise : exercises) {
            String[] sentencesIdArray = currentSentences.stream()
                    .map(String::valueOf)
                    .toArray(String[]::new);
            exercise.setSentencesId(sentencesIdArray);
        }
        // Save all updated exercises
        return exerciseRepository.saveAll(exercises);
    }

    public List<Long> getCurrentSentencesIdsByUserId(Long userId, Long exerciseId) {
        List<Exercise> exercises = exerciseRepository.findByUserIdAndExerciseId(userId, exerciseId);
        Exercise exercise = exercises.isEmpty() ? null : exercises.get(0);
        if (exercise == null || exercise.getSentencesId() == null) {
            return Collections.emptyList();
        }
        // Convert String[] to List<Long>
        List<Long> sentencesIds = new ArrayList<>();
        for (String sentenceId : exercise.getSentencesId()) {
            sentencesIds.add(Long.parseLong(sentenceId));
        }
        return sentencesIds;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public ResponseEntity<List<Long>> getSentencesIdsByTopicsAndRules(List<Long> topicIds, List<Long> ruleIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SENTENCE_IDS_API_URL);

        if (!topicIds.isEmpty()) {
            builder.queryParam("topicIds", topicIds.toArray());
        }

        if (!ruleIds.isEmpty()) {
            builder.queryParam("ruleIds", ruleIds.toArray());
        }

        String url = builder.toUriString();

        logger.info("Requesting sentence IDs with URL: {}", url);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Long>>() {
                });

        logger.info("Received response: {}", response);

        return response;
    }

    public ResponseEntity<List<TranslationWithRuleDTO>> getTranslationsById(Long sentenceIds) {
        String url = TRANSLATIONS_BY_SENTENCE_ID_API_URL + "?sentenceIds=" + sentenceIds;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TranslationWithRuleDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TranslationWithRuleDTO>>() {});

        // Return the response entity
        return response;
    }

    public List<ExerciseResponse> getExercisesByUserId(Long userId, Long exerciseId, Boolean isActive) {
        List<Exercise> exercises = exerciseRepository.findByUserIdAndExerciseIdAndActive(userId, exerciseId, isActive);
        List<ExerciseResponse> exerciseResponses = new ArrayList<>();

        for (Exercise exercise : exercises) {
            ExerciseResponse response = new ExerciseResponse();
            response.setExerciseId(exercise.getExerciseId());
            response.setExerciseName(exercise.getExerciseName());

            // Fetch topic names
            List<String> topicNames = fetchTopicNamesByIds(Arrays.asList(exercise.getTopicsIds()));
            response.setCurrentTopicsIds(topicNames);

            // Fetch rule names
            List<String> ruleNames = fetchRuleNamesByIds(Arrays.asList(exercise.getRulesIds()));
            response.setCurrentRulesIds(ruleNames);

            response.setNumberOfSentences((long) exercise.getSentencesId().length);

            List<String> currentSentences = Arrays.asList(exercise.getCurrentSentencesId());
            response.setCurrentSentencesIds(currentSentences);

            response.setCurrentSentenceId(exercise.getCurrentSentenceId());

            exerciseResponses.add(response);
        }

        return exerciseResponses;
    }

    private List<String> fetchTopicNamesByIds(List<String> topicIds) {
        List<Long> ids = topicIds.stream().map(Long::valueOf).collect(Collectors.toList());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/getTopicsByTopicsIds")
                .queryParam("topicsIds", ids);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Topic>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Topic>>() {}
        );

        return response.getBody().stream().map(Topic::getTopicName).collect(Collectors.toList());
    }

    private List<String> fetchRuleNamesByIds(List<String> ruleIds) {
        List<Long> ids = ruleIds.stream().map(Long::valueOf).collect(Collectors.toList());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/getRulesByRuleIds")
                .queryParam("ruleIds", ids);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Rule>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Rule>>() {}
        );

        return response.getBody().stream().map(Rule::getRule).collect(Collectors.toList());
    }

    public List<Exercise> updateExercises(Long userId) {
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);
        if (exercises.isEmpty()) {
            logger.warn("No exercises found for user ID: " + userId);
            return Collections.emptyList();
        }

        for (Exercise exercise : exercises) {
            Set<Long> topicIds = Arrays.stream(exercise.getTopicsIds()).map(Long::valueOf).collect(Collectors.toSet());
            Set<Long> ruleIds = Arrays.stream(exercise.getRulesIds()).map(Long::valueOf).collect(Collectors.toSet());

            ResponseEntity<List<Long>> response = getSentencesIdsByTopicsAndRules(new ArrayList<>(topicIds), new ArrayList<>(ruleIds));
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Failed to fetch sentence IDs from the external service");
                return Collections.emptyList();
            }

            List<Long> fetchedSentenceIds = response.getBody();
            if (fetchedSentenceIds == null || fetchedSentenceIds.isEmpty()) {
                logger.warn("No sentence IDs returned from the external service for exercise ID: " + exercise.getExerciseId());
                continue;  // Skip updating this exercise if no sentence IDs are fetched
            }

            // Update the exercise with the new sentence IDs
            List<String> updatedSentences = fetchedSentenceIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            exercise.setSentencesId(updatedSentences.toArray(new String[0]));
            exercise.setHasChanged(true);
        }

        return exerciseRepository.saveAll(exercises);
    }


    @Transactional
    public boolean activateExercise(Long exerciseId, Long userId) {
        Logger logger = LoggerFactory.getLogger(LessonService.class);
        try {
            exerciseRepository.deactivateAllExercisesByUserId(userId);

            int updatedRows = exerciseRepository.activateExerciseById(exerciseId);
            logger.info("Updated rows: {}", updatedRows);

            if (updatedRows > 0) {
                String currentSentencesUrl = "http://localhost:8080/currentSentencesIds?exerciseId=" + exerciseId;
                ResponseEntity<List<Long>> currentSentencesResponse =
                        restTemplate.exchange(currentSentencesUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Long>>() {});
                List<Long> sentenceIds = currentSentencesResponse.getBody();
                logger.info("Fetched sentence IDs: {}", sentenceIds);

                String removeRandomElementUrl = "http://localhost:8080/removeRandomElement";
                ResponseEntity<PickedElementResponseDTO> pickedElementResponse =
                        restTemplate.exchange(removeRandomElementUrl, HttpMethod.POST, new HttpEntity<>(sentenceIds), PickedElementResponseDTO.class);
                PickedElementResponseDTO pickedElement = pickedElementResponse.getBody();
                Long pickedSentenceId = pickedElement.getPickedElement();
                List<Long> modifiedSentenceIds = pickedElement.getModifiedArray();
                logger.info("Picked sentence ID: {}", pickedSentenceId);
                logger.info("Modified sentence IDs: {}", modifiedSentenceIds);

                UpdateExerciseRequestForUpdate updateRequest = new UpdateExerciseRequestForUpdate();
                updateRequest.setUserId(userId);
                updateRequest.setCurrentSentenceId(pickedSentenceId);
                updateRequest.setCurrentSentencesId(modifiedSentenceIds);

                String updateExerciseUrl = "http://localhost:8080/updateExerciseByExerciseId/" + exerciseId;
                logger.info("Sending update request to URL: {}", updateExerciseUrl);
                logger.info("Update request payload: {}", new ObjectMapper().writeValueAsString(updateRequest));

                // Asynchronous PUT request
                CompletableFuture.runAsync(() -> restTemplate.put(updateExerciseUrl, updateRequest))
                        .thenAccept(result -> logger.info("Update request sent successfully to URL: {}", updateExerciseUrl))
                        .exceptionally(ex -> {
                            logger.error("Error occurred while sending update request", ex);
                            return null;
                        });

                return true;
            }
        } catch (Exception e) {
            logger.error("Error occurred while activating exercise", e);
        }

        return false;
    }

    public Void restartExercise(Long userId) {
        try {
            // Step 1: Get active exercise and current sentence ID by userId
            String activeExerciseUrl = "http://localhost:8080/exercisesByUserIdAndSentenceId?userId=" + userId + "&isActive=true";
            ResponseEntity<List<Map<String, Object>>> activeExerciseResponse = restTemplate.exchange(activeExerciseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> activeExercises = activeExerciseResponse.getBody();

            // Extract exerciseId and currentSentenceId with validation
            Long exerciseId = null;

            try {
                exerciseId = Long.parseLong(activeExercises.get(0).get("exerciseId").toString());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid format for exerciseId or currentSentenceId", e);
            }

            String currentSentencesUrl = "http://localhost:8080/currentSentencesIds?exerciseId=" + exerciseId;
            ResponseEntity<List<Long>> currentSentencesResponse =
                    restTemplate.exchange(currentSentencesUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Long>>() {
                    });
            List<Long> sentenceIds = currentSentencesResponse.getBody();
            logger.info("Fetched sentence IDs: {}", sentenceIds);

            String removeRandomElementUrl = "http://localhost:8080/removeRandomElement";
            ResponseEntity<PickedElementResponseDTO> pickedElementResponse =
                    restTemplate.exchange(removeRandomElementUrl, HttpMethod.POST, new HttpEntity<>(sentenceIds), PickedElementResponseDTO.class);
            PickedElementResponseDTO pickedElement = pickedElementResponse.getBody();
            Long pickedSentenceId = pickedElement.getPickedElement();
            List<Long> modifiedSentenceIds = pickedElement.getModifiedArray();
            logger.info("Picked sentence ID: {}", pickedSentenceId);
            logger.info("Modified sentence IDs: {}", modifiedSentenceIds);

            UpdateExerciseRequestForUpdate updateRequest = new UpdateExerciseRequestForUpdate();
            updateRequest.setUserId(userId);
            updateRequest.setCurrentSentenceId(pickedSentenceId);
            updateRequest.setCurrentSentencesId(modifiedSentenceIds);

            String updateExerciseUrl = "http://localhost:8080/updateExerciseByExerciseId/" + exerciseId;

            // Asynchronous PUT request
            CompletableFuture.runAsync(() -> restTemplate.put(updateExerciseUrl, updateRequest))
                    .thenAccept(result -> logger.info("Update request sent successfully to URL: {}", updateExerciseUrl))
                    .exceptionally(ex -> {
                        logger.error("Error occurred while sending update request", ex);
                        return null;
                    });
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Optional<Exercise> updateExerciseByExerciseId(Long exerciseId, UpdateExerciseRequest request) {
        Logger logger = LoggerFactory.getLogger(LessonService.class);
        try {
            Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);

            if (optionalExercise.isPresent()) {
                Exercise exercise = optionalExercise.get();

                if (request.getUserId() != null && !exercise.getUserId().equals(request.getUserId())) {
                    logger.warn("User ID does not match the exercise's user ID");
                    return Optional.empty();
                }

                if (request.getExerciseName() != null) {
                    exercise.setExerciseName(request.getExerciseName());
                }
                if (request.getCurrentSentenceId() != null) {
                    exercise.setCurrentSentenceId(request.getCurrentSentenceId());
                }
                if (request.getSentencesId() != null) {
                    exercise.setSentencesId(convertListToStringArray(request.getSentencesId()));
                }
                if (request.getCurrentSentencesId() != null) {
                    exercise.setCurrentSentencesId(convertListToStringArray(request.getCurrentSentencesId()));
                }
                if (request.getTopicsIds() != null) {
                    exercise.setTopicsIds(convertListToStringArray(request.getTopicsIds()));
                }
                if (request.getRulesIds() != null) {
                    exercise.setRulesIds(convertListToStringArray(request.getRulesIds()));
                }

                exerciseRepository.save(exercise);
                return Optional.of(exercise);
            } else {
                logger.warn("Exercise not found with ID: " + exerciseId);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating exercise", e);
            return Optional.empty();
        }
    }

    private void logRequestDetails(String url, Object request) {
        try {
            // Convert request object to JSON string for logging
            String requestBody = new ObjectMapper().writeValueAsString(request);
            logger.info("Sending request to URL: {}", url);
            logger.info("Request body: {}", requestBody);
        } catch (JsonProcessingException e) {
            logger.error("Error converting request object to JSON", e);
        }
    }
}