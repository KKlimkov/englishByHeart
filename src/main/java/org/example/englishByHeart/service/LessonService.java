package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.dto.PickedElementResponse;
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

    public Map<String, Object> startLesson(Long exerciseId) {
        // Step 1: Get current sentence IDs
        String currentSentencesUrl = "http://localhost:8080/currentSentencesIds?exerciseId=" + exerciseId;
        ResponseEntity<List<Long>> currentSentencesResponse =
                restTemplate.exchange(currentSentencesUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Long>>() {});
        List<Long> sentenceIds = currentSentencesResponse.getBody();

        // Step 2: Remove a random element
        String removeRandomElementUrl = "http://localhost:8080/removeRandomElement";
        ResponseEntity<PickedElementResponse> pickedElementResponse =
                restTemplate.exchange(removeRandomElementUrl, HttpMethod.POST, new HttpEntity<>(sentenceIds), PickedElementResponse.class);
        PickedElementResponse pickedElement = pickedElementResponse.getBody();

        // Step 3: Get lesson details
        Long pickedSentenceId = pickedElement.getPickedElement();
        Lesson lesson = getLesson(pickedSentenceId);

        // Step 4: Construct the final response
        return Map.of(
                "modifiedArray", pickedElement.getModifiedArray(),
                "sentenceId", lesson.getSentenceId(),
                "learningSentence", lesson.getLearningSentence(),
                "comment", lesson.getComment(),
                "userLink", lesson.getUserLink(),
                "translations", lesson.getTranslations()
        );
    }
}

