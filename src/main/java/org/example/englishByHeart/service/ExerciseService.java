package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Exercise;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.SentenceRule;
import org.example.englishByHeart.dto.CreateExerciseRequest;
import org.example.englishByHeart.dto.TranslationWithRuleDTO;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
        Exercise exercise = new Exercise();
        exercise.setUserId(request.getUserId());
        exercise.setSentenceName(request.getSentenceName());
        exercise.setCurrentSentencesId(convertListToStringArray(request.getCurrentSentencesId()));
        exercise.setCurrentTopicsIds(convertListToStringArray(request.getCurrentTopicsIds()));
        exercise.setCurrentRulesIds(convertListToStringArray(request.getCurrentRulesIds()));
        return exerciseRepository.save(exercise);
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
            exercise.setCurrentSentencesId(sentencesIdArray);
        }
        // Save all updated exercises
        return exerciseRepository.saveAll(exercises);
    }

    public List<Long> getCurrentSentencesIdsByUserId(Long userId) {
        List<Exercise> exercises = exerciseRepository.findByUserId(userId);
        Exercise exercise = exercises.isEmpty() ? null : exercises.get(0);
        if (exercise == null || exercise.getCurrentSentencesId() == null) {
            return Collections.emptyList();
        }
        // Convert String[] to List<Long>
        List<Long> sentencesIds = new ArrayList<>();
        for (String sentenceId : exercise.getCurrentSentencesId()) {
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

        // Construct the URL with the query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SENTENCE_IDS_API_URL)
                .queryParam("topicIds", topicIds.toArray())
                .queryParam("ruleIds", ruleIds.toArray());

        String url = builder.toUriString();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Long>>() {});

        return response;
    }

    public ResponseEntity<Sentence> getSentenceById(Long sentenceId) {
        // Construct the URL for the specific sentenceId
        String url = SENTENCE_BY_ID_API_URL + "/" + sentenceId;

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HTTP request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Make the HTTP GET request
        ResponseEntity<Sentence> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Sentence.class);

        // Return the response entity
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

}