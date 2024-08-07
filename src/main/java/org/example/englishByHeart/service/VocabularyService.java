package org.example.englishByHeart.service;

import org.example.englishByHeart.dto.SentenceDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VocabularyService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

    private static final String SENTENCE_API_URL = "http://localhost:8080/api/sentence"; // Update with your API URL
    private static final String TRANSLATION_API_URL = "http://localhost:8080/translations"; // Update with your API URL
    private static final String TRANSLATIONS_API_URL = "http://localhost:8080/translations/sentenceIds"; // Update with your API URL
    private static final String RULE_API_URL = "http://localhost:8080/getTranslationIdsByRuleIds"; // Update with your API URL
    private static final String SENTENCES_API_URL = "http://localhost:8080/api/sentence/search"; // Update with your API URL
    private static final String UPDATE_TRANSLATIONS_API_URL = "http://localhost:8080/translations/updateBySentenceId";

    @Autowired
    private RestTemplate restTemplate;


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

        SentenceDTO sentenceDTO = new SentenceDTO();
        sentenceDTO.setUserLink(request.getUserLink());
        sentenceDTO.setLearningSentence(request.getLearningSentence());
        sentenceDTO.setComment(request.getComment());
        sentenceDTO.setUserId(request.getUserId());
        sentenceDTO.setTopicsIds(request.getTopicsIds());

        List<Long> rulesIds = new ArrayList<>();
        Set<Long> setRulesIds = new HashSet<>();

        List<TranslationRequestForAdd> translations = request.getTranslations();
        if (translations != null) {
            for (TranslationRequestForAdd translation : translations) {
                // Extract ruleIds from each TranslationRequestForAdd and add them to setRulesIds
                List<Long> ruleIds = translation.getRuleIds();
                if (ruleIds != null) {
                    setRulesIds.addAll(ruleIds);
                }
            }
        }

        rulesIds.addAll(setRulesIds);
        sentenceDTO.setRulesIds(rulesIds);

        HttpEntity<SentenceDTO> requestEntity = new HttpEntity<>(sentenceDTO, headers);

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

    public ResponseEntity<String> updateSentence(Long sentenceId, VocabularyRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the SentenceDTO
        SentenceDTO sentenceDTO = new SentenceDTO();
        sentenceDTO.setSentenceId(sentenceId);
        sentenceDTO.setUserLink(request.getUserLink());
        sentenceDTO.setLearningSentence(request.getLearningSentence());
        sentenceDTO.setComment(request.getComment());
        sentenceDTO.setUserId(request.getUserId());
        sentenceDTO.setTopicsIds(request.getTopicsIds());

        // Collect all unique rule IDs
        Set<Long> setRulesIds = new HashSet<>();
        List<TranslationRequestForAdd> translations = request.getTranslations();
        if (translations != null) {
            for (TranslationRequestForAdd translation : translations) {
                List<Long> ruleIds = translation.getRuleIds();
                if (ruleIds != null) {
                    setRulesIds.addAll(ruleIds);
                }
            }
        }

        // Convert Set to List and set in DTO
        List<Long> rulesIds = new ArrayList<>(setRulesIds);
        sentenceDTO.setRulesIds(rulesIds);

        // Log the SentenceDTO
        logger.info("Updating Sentence: URL={}, Payload={}", SENTENCE_API_URL + "/" + sentenceId, sentenceDTO);

        // Send the request to update the sentence
        HttpEntity<SentenceDTO> requestEntity = new HttpEntity<>(sentenceDTO, headers);
        String sentenceUrl = SENTENCE_API_URL + "/" + sentenceId;
        ResponseEntity<String> response = restTemplate.exchange(sentenceUrl, HttpMethod.PUT, requestEntity, String.class);

        // Log the response for updating the sentence
        logger.info("Update Sentence Response: Status={}, Body={}", response.getStatusCode(), response.getBody());

        // Check if the sentence update was successful
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            // Prepare TranslationRequests
            List<TranslationRequest> translationRequests = translations.stream()
                    .map(translation -> {
                        TranslationRequest translationRequest = new TranslationRequest();
                        translationRequest.setTranslation(translation.getTranslation());
                        translationRequest.setRuleIds(translation.getRuleIds());
                        translationRequest.setSentenceId(sentenceId);
                        return translationRequest;
                    })
                    .collect(Collectors.toList());

            // Log the TranslationRequests
            logger.info("Preparing to Update Translations: Payload={}", translationRequests);

            // Send the request to update translations
            HttpEntity<List<TranslationRequest>> translationRequestEntity = new HttpEntity<>(translationRequests, headers);
            String translationsUrl = UPDATE_TRANSLATIONS_API_URL;
            logger.info("Updating Translations: URL={}, Payload={}", translationsUrl, translationRequests);

            ResponseEntity<String> translationResponse = restTemplate.exchange(translationsUrl, HttpMethod.PUT, translationRequestEntity, String.class);

            // Log the response for updating translations
            logger.info("Update Translations Response: Status={}, Body={}", translationResponse.getStatusCode(), translationResponse.getBody());

            if (translationResponse.getStatusCode() != HttpStatus.OK) {
                return translationResponse; // Return the response from updating translations if not OK
            }
        } else {
            logger.warn("Sentence update failed with status: {}", response.getStatusCode());
        }

        return response; // Return the response from updating the sentence
    }

}