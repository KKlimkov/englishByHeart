package org.example.englishByHeart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.englishByHeart.service.VocabularyService;
import org.example.englishByHeart.dto.SentenceDTO;
import org.example.englishByHeart.dto.TranslationRequestForAdd;
import org.example.englishByHeart.dto.VocabularyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final ObjectMapper objectMapper;
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

    @Autowired
    public VocabularyController(VocabularyService vocabularyService, ObjectMapper objectMapper) {
        this.vocabularyService = vocabularyService;
        this.objectMapper = objectMapper;
    }


    @PutMapping("/update/sentence/{sentenceId}")
    public ResponseEntity<CustomResponse<Long>> updateSentence(@PathVariable Long sentenceId, @RequestBody VocabularyRequest request) throws JsonProcessingException {
        ResponseEntity<String> sentenceResponse = vocabularyService.updateSentence(sentenceId, request);
        if (sentenceResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(sentenceResponse.getStatusCode())
                    .body(new CustomResponse<>(false, "Sentence update failed", null));
        }

        CustomResponse<Long> customResponse = new CustomResponse<>(true, "Sentence updated successfully", sentenceId);
        return ResponseEntity.ok().body(customResponse);
    }

}


