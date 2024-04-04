package org.example.englishByHeart.Service;

import org.example.englishByHeart.controller.CustomResponse;
import org.example.englishByHeart.controller.SentenceIdResponse;
import org.example.englishByHeart.domain.*;
import org.example.englishByHeart.dto.SentenceDTO;
import org.example.englishByHeart.repos.RuleRepository;
import org.example.englishByHeart.repos.SentenceRepository;
import org.example.englishByHeart.repos.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<Sentence> getAllSentences() {
        return sentenceRepository.findAll();
    }

    public Optional<Sentence> getSentenceById(Long id) {
        return sentenceRepository.findById(id);
    }

    public CustomResponse<SentenceIdResponse> createSentence(SentenceDTO sentenceDTO) {

        CustomResponse<SentenceIdResponse> response; // Specify type argument here

        try {
            Sentence sentence = new Sentence();
            sentence.setLearningSentence(sentenceDTO.getLearningSentence());
            sentence.setComment(sentenceDTO.getComment());
            sentence.setUserLink(sentenceDTO.getUserLink());
            sentence.setUserId(sentenceDTO.getUserId());

            // Save the sentence and get the sentenceId
            //sentence.setSentenceId(null);

            List<SentenceTopic> sentenceTopics = new ArrayList<>();

            for (Long topicsId : sentenceDTO.getTopicsIds()) {
                Topic topic = topicRepository.findById(topicsId)
                        .orElseThrow(() -> new RuntimeException("Topic not found with id: " + topicsId));
                SentenceTopic sentenceTopic = new SentenceTopic();
                sentenceTopic.setSentence(sentence);
                sentenceTopic.setTopic(topic);
                sentenceTopics.add(sentenceTopic);
            }

            sentence.setSentenceTopics(sentenceTopics);

            Sentence createdSentence = sentenceRepository.save(sentence);
            Long sentenceId = createdSentence.getSentenceId();

            // Populate the response
            response = new CustomResponse<>(true, "", new SentenceIdResponse(sentenceId));

        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response = new CustomResponse<>(false, "Failed to create sentence: " + e.getMessage(), null);
        }

        return response;
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