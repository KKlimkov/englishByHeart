package org.example.englishByHeart.controller;

import org.example.englishByHeart.Service.SentenceService;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.dto.SentenceDTO;
import org.example.englishByHeart.repos.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sentence")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @GetMapping
    public List<Sentence> getAllSentences() {
        return sentenceService.getAllSentences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sentence> getSentenceById(@PathVariable Long id) {
        Optional<Sentence> sentenceOptional = sentenceService.getSentenceById(id);
        return sentenceOptional.map(sentence -> new ResponseEntity<>(sentence, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CustomResponse<SentenceIdResponse>> createSentence(@RequestBody SentenceDTO sentence) {
        CustomResponse<SentenceIdResponse> response = sentenceService.createSentence(sentence);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sentence> updateSentence(@PathVariable Long id, @RequestBody Sentence updatedSentence) {
        return sentenceService.updateSentence(id, updatedSentence);
    }

    // Other CRUD endpoints as needed

}
