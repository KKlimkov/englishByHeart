package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.dto.PickedElementResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LessonService {

    @Autowired
    private RestTemplate restTemplate;

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
        // Step 1: Get active exercise and current sentence ID by userId
        String activeExerciseUrl = "http://localhost:8080/exercisesByUserIdAndSentenceId?userId=" + userId + "&isActive=true";
        ResponseEntity<List<Map<String, Long>>> activeExerciseResponse = restTemplate.exchange(activeExerciseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Long>>>() {});
        List<Map<String, Long>> activeExercises = activeExerciseResponse.getBody();

        if (activeExercises == null || activeExercises.isEmpty()) {
            throw new RuntimeException("No active exercises found for user " + userId);
        }

        Long exerciseId = activeExercises.get(0).get("exerciseId");
        Long currentSentenceId = activeExercises.get(0).get("currentSentenceId");

        // Step 2: Get lesson details
        String lessonDetailsUrl = "http://localhost:8080/api/lesson/getLesson/" + currentSentenceId;
        ResponseEntity<Lesson> lessonResponse = restTemplate.exchange(lessonDetailsUrl, HttpMethod.GET, null, Lesson.class);
        Lesson lesson = lessonResponse.getBody();

        // Step 3: Construct the final response
        return Map.of(
                "sentenceId", lesson.getSentenceId(),
                "learningSentence", lesson.getLearningSentence(),
                "comment", lesson.getComment(),
                "userLink", lesson.getUserLink(),
                "translations", lesson.getTranslations()
        );
    }
}

