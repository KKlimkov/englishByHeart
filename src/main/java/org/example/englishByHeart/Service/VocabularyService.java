package org.example.englishByHeart.Service;

import org.example.englishByHeart.dto.TranslationRequest;
import org.example.englishByHeart.dto.VocabularyRequest;
import org.example.englishByHeart.dto.TranslationRequestForAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class VocabularyService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

    private static final String SENTENCE_API_URL = "http://localhost:8080/api/sentence"; // Update with your API URL
    private static final String TRANSLATION_API_URL = "http://localhost:8080/translations"; // Update with your API URL

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> callExternalControllerForSentence(VocabularyRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VocabularyRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(SENTENCE_API_URL, HttpMethod.POST, requestEntity, String.class);

        return response;
    }

    public ResponseEntity<String> callExternalControllerForTranslations(TranslationRequestForAdd translations, Long newSentenceId) {
        // Create a new list to hold TranslationRequest objects

        // Populate the new list with translations and set the sentenceId
        TranslationRequest translation = new TranslationRequest();

        translation.setTranslation(translations.getTranslation());
        translation.setRuleIds(translations.getRuleIds());
        translation.setSentenceId(newSentenceId); // Set the sentenceId


        System.out.println("I transformed" + translation);

        logger.info("Translation requests: {}", translations);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with the updated translations
        HttpEntity<TranslationRequest> requestEntity = new HttpEntity<>(translation, headers);

        // Make the HTTP POST request to the translation endpoint
        ResponseEntity<String> response = restTemplate.exchange(TRANSLATION_API_URL, HttpMethod.POST, requestEntity, String.class);

        return response;
    }
}