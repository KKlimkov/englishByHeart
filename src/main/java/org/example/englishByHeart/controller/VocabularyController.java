package org.example.englishByHeart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.englishByHeart.Service.VocabularyService;
import org.example.englishByHeart.dto.TranslationRequestForAdd;
import org.example.englishByHeart.dto.VocabularyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    @Autowired
    private VocabularyService vocabularyService;

    @PostMapping("/add/sentence")
    public ResponseEntity<String> addSentence(@RequestBody VocabularyRequest request) throws JsonProcessingException {
        ResponseEntity<String> sentenceResponse = vocabularyService.callExternalControllerForSentence(request);
        if (sentenceResponse.getStatusCode() != HttpStatus.CREATED) {
            return sentenceResponse; // Return if sentence creation fails
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBodyJson = objectMapper.readTree(sentenceResponse.getBody());
        Long newSentenceId = responseBodyJson.get("data").get("sentenceId").asLong();
        // Extract the new sentenceId from the response body

        // Iterate over each translation in the request
        for (TranslationRequestForAdd translation : request.getTranslations()) {
            // Call the external controller's post method for each translation
            ResponseEntity<String> translationResponse = vocabularyService.callExternalControllerForTranslations(translation, newSentenceId);
            // Check if the translation response indicates success, and handle errors if necessary
            if (translationResponse.getStatusCode() != HttpStatus.CREATED) {
                return translationResponse;
            }
        }

        // If all translations were successful, return a success response
        return ResponseEntity.ok("All translations added successfully.");
    }

}


