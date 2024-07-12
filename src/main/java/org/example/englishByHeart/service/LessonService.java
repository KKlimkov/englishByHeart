package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.dto.PickedElementResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LessonService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);


    public Lesson getLesson(Long sentenceId) {
        // Call the sentence service
        String sentenceUrl = "http://localhost:8080/api/sentence/sentenceById/" + sentenceId;
        Sentence sentence = restTemplate.getForObject(sentenceUrl, Sentence.class);

        // Call the translation service
        String translationUrl = "http://localhost:8080/translations/translations?sentenceIds=" + sentenceId;
        ResponseEntity<List<Translation>> translationResponse =
                restTemplate.exchange(translationUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Translation>>() {});
        List<Translation> translations = translationResponse.getBody();

        // Combine the results
        Lesson lesson = new Lesson();
        lesson.setSentenceId(sentence.getSentenceId());
        lesson.setUserId(sentence.getUserId());
        lesson.setLearningSentence(sentence.getLearningSentence());
        lesson.setComment(sentence.getComment());
        lesson.setUserLink(sentence.getUserLink());
        lesson.setTranslations(translations);

        return lesson;
    }

    public Map<String, Object> startLesson(Long userId) {
        try {
            // Step 1: Get active exercise and current sentence ID by userId
            String activeExerciseUrl = "http://localhost:8080/exercisesByUserIdAndSentenceId?userId=" + userId + "&isActive=true";
            logger.info("Fetching active exercises from URL: {}", activeExerciseUrl);

            ResponseEntity<List<Map<String, Object>>> activeExerciseResponse = restTemplate.exchange(
                    activeExerciseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            List<Map<String, Object>> activeExercises = activeExerciseResponse.getBody();

            if (activeExercises == null || activeExercises.isEmpty()) {
                logger.warn("No active exercises found for user {}", userId);
                // Return a custom response indicating no active exercises
                return Map.of("status", "error", "message", "No active exercises found for user " + userId);
            }

            // Extract exerciseId and currentSentenceId with validation
            Long exerciseId = null;
            Long currentSentenceId = null;

            try {
                exerciseId = Long.parseLong(activeExercises.get(0).get("exerciseId").toString());
                currentSentenceId = Long.parseLong(activeExercises.get(0).get("currentSentenceId").toString());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid format for exerciseId or currentSentenceId", e);
            }

            // Step 2: Get lesson details
            String lessonDetailsUrl = "http://localhost:8080/api/lesson/getLesson/" + currentSentenceId;
            ResponseEntity<Lesson> lessonResponse = restTemplate.exchange(
                    lessonDetailsUrl,
                    HttpMethod.GET,
                    null,
                    Lesson.class
            );
            Lesson lesson = lessonResponse.getBody();

            if (lesson == null) {
                throw new RuntimeException("No lesson details found for sentence " + currentSentenceId);
            }

            // Step 3: Construct the final response
            return Map.of(
                    "sentenceId", lesson.getSentenceId(),
                    "learningSentence", lesson.getLearningSentence(),
                    "comment", lesson.getComment(),
                    "userLink", lesson.getUserLink(),
                    "translations", lesson.getTranslations()
            );
        } catch (RestClientException e) {
            throw new RuntimeException("Request processing failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred: " + e.getMessage(), e);
        }
    }

    public void updateLesson(Long userId) {
        try {
            // Step 1: Get active exercise and current sentence ID by userId
            String activeExerciseUrl = "http://localhost:8080/exercisesByUserIdAndSentenceId?userId=" + userId + "&isActive=true";
            ResponseEntity<List<Map<String, Object>>> activeExerciseResponse = restTemplate.exchange(activeExerciseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> activeExercises = activeExerciseResponse.getBody();

            if (activeExercises == null || activeExercises.isEmpty()) {
                throw new RuntimeException("No active exercises found for user " + userId);
            }

            Map<String, Object> activeExercise = activeExercises.get(0);
            Long exerciseId = ((Number) activeExercise.get("exerciseId")).longValue();
            List<Long> currentSentencesIds = (List<Long>) activeExercise.get("currentSentencesIds");

            if (currentSentencesIds == null || currentSentencesIds.isEmpty()) {
                throw new AllLessonsDoneException("All lessons have been done!");
            }

            // Step 2: Remove a random element from currentSentencesIds
            String removeRandomElementUrl = "http://localhost:8080/removeRandomElement";
            ResponseEntity<Map<String, Object>> removeElementResponse = restTemplate.exchange(
                    removeRandomElementUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(currentSentencesIds),
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> removeElementResult = removeElementResponse.getBody();
            Long pickedElement = ((Number) removeElementResult.get("pickedElement")).longValue();
            List<Long> modifiedArray = (List<Long>) removeElementResult.get("modifiedArray");

            // Step 3: Update the exercise with the new current sentence ID and modified list
            String updateExerciseUrl = "http://localhost:8080/updateExerciseByExerciseId/" + exerciseId;
            Map<String, Object> updateExerciseRequest = Map.of(
                    "userId", userId,
                    "currentSentenceId", pickedElement,
                    "currentSentencesId", modifiedArray
            );
            restTemplate.exchange(updateExerciseUrl, HttpMethod.PUT, new HttpEntity<>(updateExerciseRequest), Void.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Request processing failed: " + e.getMessage(), e);
        } catch (AllLessonsDoneException e) {
            throw e; // Rethrow to be handled by the controller
        } catch (Exception e) {
            throw new RuntimeException("An error occurred: " + e.getMessage(), e);
        }
    }

}


