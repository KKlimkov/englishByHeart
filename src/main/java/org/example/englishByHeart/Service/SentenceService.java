package org.example.englishByHeart.Service;

import org.example.englishByHeart.controller.CustomResponse;
import org.example.englishByHeart.controller.SentenceIdResponse;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.repos.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    public List<Sentence> getAllSentences() {
        return sentenceRepository.findAll();
    }

    public Optional<Sentence> getSentenceById(Long id) {
        return sentenceRepository.findById(id);
    }

    public CustomResponse<SentenceIdResponse> createSentence(Sentence sentence) {
        CustomResponse<SentenceIdResponse> response = new CustomResponse<>();

        try {
            // Save the sentence and get the sentenceId
            sentence.setSentenceId(null);
            Sentence createdSentence = sentenceRepository.save(sentence);
            Long sentenceId = createdSentence.getSentenceId();

            // Populate the response
            response.setSuccess(true);
            response.setErrorMessage("");

            SentenceIdResponse sentenceIdResponse = new SentenceIdResponse();
            sentenceIdResponse.setSentenceId(sentenceId);
            response.setData(sentenceIdResponse);

            return response;
        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response.setSuccess(false);
            response.setErrorMessage("Failed to create sentence: " + e.getMessage());
            return response;
        }
    }

    public ResponseEntity<Sentence> updateSentence(Long id, Sentence updatedSentence) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findById(id);
        if (sentenceOptional.isPresent()) {
            updatedSentence.setSentenceId(id);
            Sentence savedSentence = sentenceRepository.save(updatedSentence);
            return new ResponseEntity<>(savedSentence, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}