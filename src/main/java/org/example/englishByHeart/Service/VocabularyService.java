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
import java.util.stream.Collectors;

@Service
public class VocabularyService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

    private static final String SENTENCE_API_URL = "http://localhost:8080/api/sentence"; // Update with your API URL
    private static final String TRANSLATION_API_URL = "http://localhost:8080/translations"; // Update with your API URL
    private static final String TRANSLATIONS_API_URL = "http://localhost:8080/translations/sentenceIds"; // Update with your API URL
    private static final String RULE_API_URL = "http://localhost:8080/getTranslationIdsByRuleIds"; // Update with your API URL
    private static final String SENTENCES_API_URL = "http://localhost:8080/api/sentence/search"; // Update with your API URL

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> getTranslationsByRulesIds(List<Long> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(request, headers);

        // Convert list of ruleIds to a comma-separated string
        String ruleIdsString = request.stream()
                .map(Object::toString)
                .collect(Collectors.joining("&ruleIds=", "?ruleIds=", ""));

        // Construct the URL with the ruleIds query parameter
        String url = RULE_API_URL + ruleIdsString;

        System.out.println(url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response;
    }

    public ResponseEntity<String> getSentencesIdsByTranslationsIds(List<Long> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(request, headers);

        // Convert list of ruleIds to a comma-separated string
        String translationIds = request.stream()
                .map(Object::toString)
                .collect(Collectors.joining("&translationIds=", "?translationIds=", ""));

        // Construct the URL with the ruleIds query parameter
        String url = TRANSLATIONS_API_URL + translationIds;

        System.out.println(url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response;
    }

    public ResponseEntity<String> getSentencesBySentenceIds(List<Long> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(request, headers);

        // Convert list of ruleIds to a comma-separated string
        String sentenceIds = request.stream()
                .map(Object::toString)
                .collect(Collectors.joining("&sentenceIds=", "?sentenceIds=", ""));

        // Construct the URL with the ruleIds query parameter
        String url = SENTENCES_API_URL + sentenceIds;

        System.out.println(url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response;
    }

    public ResponseEntity<String> getSentencesByRulesIds(List<Long> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(request, headers);

        // Convert list of ruleIds to a comma-separated string
        String ruleIdsString = request.stream()
                .map(Object::toString)
                .collect(Collectors.joining("&ruleIds=", "?ruleIds=", ""));

        // Construct the URL with the ruleIds query parameter
        String url = RULE_API_URL + ruleIdsString;

        System.out.println(url);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response;
    }

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