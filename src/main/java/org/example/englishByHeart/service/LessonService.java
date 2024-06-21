package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

        // Adding rules and links to each translation
        for (Translation translation : translations) {
            List<RuleAndLink> rulesAndLinks = translation.getTranslationRuleLinks().stream()
                    .map(tr -> new RuleAndLink(tr.getRule().getRule(), tr.getRule().getLink()))
                    .toList();
            translation.setRulesAndLinks(rulesAndLinks);
        }

        return lesson;
    }
}

