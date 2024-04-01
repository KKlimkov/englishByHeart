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

    public ResponseEntity<String> callExternalControllerForTranslations(List<TranslationRequestForAdd> translations, Long newSentenceId) {
        List<ResponseEntity<String>> responses = new ArrayList<>();

        for (TranslationRequestForAdd translation : translations) {
            TranslationRequest translationRequest = new TranslationRequest();
            translationRequest.setTranslation(translation.getTranslation());
            translationRequest.setRuleIds(translation.getRuleIds());
            translationRequest.setSentenceId(newSentenceId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TranslationRequest> requestEntity = new HttpEntity<>(translationRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(TRANSLATION_API_URL, HttpMethod.POST, requestEntity, String.class);
            responses.add(response);

            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("Service: Translation request failed: " + response.getBody());
                return response;
            }
        }

        // If all requests were successful, return a success response
        return ResponseEntity.ok().build();
    }
}