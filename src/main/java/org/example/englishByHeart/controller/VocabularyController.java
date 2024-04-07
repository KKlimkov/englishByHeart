package org.example.englishByHeart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.englishByHeart.Service.VocabularyService;
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

    @GetMapping("/sentencesByRules")
    public ResponseEntity<List<SentenceDTO>> getSentencesByRulesIds(@RequestParam("request") List<Long> request) throws JsonProcessingException {
        ResponseEntity<String> translationsResponse = vocabularyService.getTranslationsByRulesIds(request);

        List<Long> translationsIds = objectMapper.readValue(translationsResponse.getBody(), new TypeReference<List<Long>>() {});

        ResponseEntity<String> sentencesIdsByTranslationsIdsResponse = vocabularyService.getSentencesIdsByTranslationsIds(translationsIds);

        List<Long> sentenceIds = objectMapper.readValue(sentencesIdsByTranslationsIdsResponse.getBody(), new TypeReference<List<Long>>() {});

        ResponseEntity<String> sentencesResponse = vocabularyService.getSentencesBySentenceIds(sentenceIds);

        List<SentenceDTO> sentences = objectMapper.readValue(sentencesResponse.getBody(), new TypeReference<List<SentenceDTO>>() {});

        // Return the list of sentence IDs as ResponseEntity
        return ResponseEntity.ok().body(sentences);

    }

}


