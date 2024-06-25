package org.example.englishByHeart.service;

import jakarta.transaction.Transactional;
import org.example.englishByHeart.domain.*;
import org.example.englishByHeart.dto.CreateExerciseRequest;
import org.example.englishByHeart.dto.ExerciseResponse;
import org.example.englishByHeart.dto.TranslationWithRuleDTO;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
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

    public List<ExerciseResponse> getExercisesByUserId(Long userId, Long exerciseId) {
        List<Exercise> exercises = exerciseRepository.findByUserIdAndExerciseId(userId, exerciseId);
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
        // Deactivate all exercises for the user
        exerciseRepository.deactivateAllExercisesByUserId(userId);

        // Activate the new exercise
        int updatedRows = exerciseRepository.activateExerciseById(exerciseId);

        return updatedRows > 0;
    }
}