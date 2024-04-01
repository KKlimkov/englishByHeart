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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    @Autowired
    private VocabularyService vocabularyService;

    @PostMapping("/add/sentence")
    public ResponseEntity<CustomResponse<Long>> addSentence(@RequestBody VocabularyRequest request) throws JsonProcessingException {
        ResponseEntity<String> sentenceResponse = vocabularyService.callExternalControllerForSentence(request);
        if (sentenceResponse.getStatusCode() != HttpStatus.CREATED) {
            return ResponseEntity.status(sentenceResponse.getStatusCode())
                    .body(new CustomResponse<>(false, "Sentence creation failed", null));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBodyJson = objectMapper.readTree(sentenceResponse.getBody());
        Long newSentenceId = responseBodyJson.get("data").get("sentenceId").asLong();

        List<ResponseEntity<String>> responses = new ArrayList<>();
        for (TranslationRequestForAdd translation : request.getTranslations()) {
            ResponseEntity<String> translationResponse = vocabularyService.callExternalControllerForTranslations(Collections.singletonList(translation), newSentenceId);
            responses.add(translationResponse);

            if (translationResponse.getStatusCode() != HttpStatus.OK) {
                System.err.println("Controller: Translation request failed: " + translationResponse.getBody());
            }
        }

        CustomResponse<Long> customResponse = new CustomResponse<>(true, null, newSentenceId);
        return ResponseEntity.ok().body(customResponse);
    }

}


