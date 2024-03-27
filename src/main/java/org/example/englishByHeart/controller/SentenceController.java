package org.example.englishByHeart.controller;

import jakarta.validation.Valid;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.repos.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sentences")
public class SentenceController {

    @Autowired
    private SentenceRepository sentenceRepository;

    @GetMapping
    public List<Sentence> getAllSentences() {
        return sentenceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sentence> getSentenceById(@PathVariable Integer id) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findById(id);
        return sentenceOptional.map(sentence -> new ResponseEntity<>(sentence, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CustomResponse<SentenceIdResponse>> createSentence(@RequestBody Sentence sentence) {
        CustomResponse<SentenceIdResponse> response = new CustomResponse<>();

        try {
            // Save the sentence and get the sentenceId
            sentence.setSentenceId(null);
            Sentence createdSentence = sentenceRepository.save(sentence);
            Integer sentenceId = createdSentence.getSentenceId();

            // Populate the response
            response.setSuccess(true);
            response.setErrorMessage("");

            SentenceIdResponse sentenceIdResponse = new SentenceIdResponse();
            sentenceIdResponse.setSentenceId(sentenceId);
            response.setData(sentenceIdResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response.setSuccess(false);
            response.setErrorMessage("Failed to create sentence: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sentence> updateSentence(@PathVariable Integer id, @RequestBody Sentence updatedSentence) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findById(id);
        if (sentenceOptional.isPresent()) {
            updatedSentence.setSentenceId(id);
            Sentence savedSentence = sentenceRepository.save(updatedSentence);
            return new ResponseEntity<>(savedSentence, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Other CRUD endpoints as needed

    @PostMapping("/addSentence")
    public ResponseEntity<CustomResponse<SentenceIdResponse>> addSentenceWithTranslations(@Valid @RequestBody AddSentenceWithTranslationsRequest request, BindingResult result) {
        CustomResponse<SentenceIdResponse> response = new CustomResponse<>();

        // Check if there are validation errors in the request
        if (result.hasErrors()) {
            response.setSuccess(false);
            response.setErrorMessage("Validation failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // Step 1: Create the sentence
            ResponseEntity<CustomResponse<SentenceIdResponse>> sentenceResponse = createSentence(request);
            if (!sentenceResponse.getStatusCode().is2xxSuccessful()) {
                // If creating sentence fails, return the error response
                return ResponseEntity.status(sentenceResponse.getStatusCode()).body(sentenceResponse.getBody());
            }

            // Get the sentenceId from the response
            Integer sentenceId = sentenceResponse.getBody().getData().getSentenceId();

            // Step 2: Create translations
            List<Translation> translations = createTranslations(request.getTranslations(), sentenceId);
            ResponseEntity<CustomArrayResponse<Integer>> translationResponse = createTranslation(translations, result);

            if (!translationResponse.getStatusCode().is2xxSuccessful()) {
                // If creating translations fails, return the error response
                return ResponseEntity.status(translationResponse.getStatusCode()).body(new CustomResponse<>(false, translationResponse.getBody().getErrorMessage(), null));
            }

            // Populate the response with the sentenceId
            response.setSuccess(true);
            response.setErrorMessage("");
            SentenceIdResponse sentenceIdResponse = new SentenceIdResponse();
            sentenceIdResponse.setSentenceId(sentenceId);
            response.setData(sentenceIdResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response.setSuccess(false);
            response.setErrorMessage("Failed to add sentence with translations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
