package org.example.englishByHeart.service;

import org.example.englishByHeart.controller.CustomResponse;
import org.example.englishByHeart.dto.SentenceDtoTable;
import org.example.englishByHeart.dto.SentenceIdResponse;
import org.example.englishByHeart.domain.*;
import org.example.englishByHeart.dto.SentenceDTO;
import org.example.englishByHeart.dto.TranslationWithRuleDTO;
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

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private TopicService topicService;

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

            List<SentenceRule> sentenceRules= new ArrayList<>();

            for (Long rulesId : sentenceDTO.getRulesIds()) {
                Rule rule = ruleRepository.findById(rulesId)
                        .orElseThrow(() -> new RuntimeException("Rule not found with id: " + rulesId));
                SentenceRule sentenceRule= new SentenceRule();
                sentenceRule.setSentence(sentence);
                sentenceRule.setRule(rule);
                sentenceRules.add(sentenceRule);
            }
            sentence.setSentenceRules(sentenceRules);

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

    public List<Sentence> getSentencesByRule(Long ruleId) {
        return sentenceRepository.findByRuleId(ruleId);
    }

    public Sentence getSentenceBySentenceId(Long sentenceId) {
        Optional<Sentence> optionalSentence = sentenceRepository.findBySentenceId(sentenceId);
        return optionalSentence.orElseThrow(() -> new NoSuchElementException("Sentence not found with id: " + sentenceId));
    }

    public List<Sentence> getSentencesByRules(List<Long> ruleIds) {
        return sentenceRepository.findByRuleIds(ruleIds);
    }

    public List<Sentence> getSentencesByTopicId(Long topicId) {
        return sentenceRepository.findByTopicId(topicId);
    }

    public List<Sentence> getSentencesByTopicIds(List<Long> topicIds) {
        return sentenceRepository.findByTopicIds(topicIds);
    }

    public List<Sentence> getSentencesByTopicsAndRules(List<Long> topicIds, List<Long> ruleIds) {
        return sentenceRepository.findByTopicsAndRules(topicIds, ruleIds);
    }

    public Set<Long> getSentenceIdsByTopicsAndRules(List<Long> topicIds, List<Long> ruleIds) {
        if (topicIds != null && ruleIds == null) {
            // Handle case where only topicIds are provided
            return new HashSet<>(sentenceRepository.findSentenceIdsByTopicIds(topicIds));
        }

        if (topicIds == null && ruleIds != null) {
            // Handle case where only ruleIds are provided
            return new HashSet<>(sentenceRepository.findSentenceIdsByRuleIds(ruleIds));
        }

        // Handle case where both topicIds and ruleIds are provided
        return new HashSet<>(sentenceRepository.findSentenceIdsByTopicsAndRules(topicIds, ruleIds));
    }

    public List<Sentence> searchSentences(Long userId, Set<Long> sentenceIds, List<Long> sentenceTopics) {
        if (userId != null && sentenceIds != null && !sentenceIds.isEmpty() && sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by userId, sentenceIds, and sentenceTopics
            return sentenceRepository.findByUserIdAndSentenceIdInAndSentenceTopics(userId, sentenceIds, sentenceTopics);
        } else if (userId != null && sentenceIds != null && !sentenceIds.isEmpty()) {
            // Search by userId and sentenceIds
            return sentenceRepository.findByUserIdAndSentenceIdIn(userId, sentenceIds);
        } else if (userId != null && sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by userId and sentenceTopics
            return sentenceRepository.findByUserIdAndSentenceTopicsIn(userId, sentenceTopics);
        } else if (userId != null) {
            // Search by userId
            return sentenceRepository.findByUserId(userId);
        } else if (sentenceIds != null && !sentenceIds.isEmpty()) {
            // Search by sentenceIds
            return sentenceRepository.findBySentenceIdIn(sentenceIds);
        } else if (sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by sentenceTopics
            return sentenceRepository.findBySentenceTopicsIn(sentenceTopics);
        } else {
            // No search criteria provided, return empty list
            return Collections.emptyList();
        }
    }

    public List<SentenceDtoTable> getFullSentencesByUserId(Long userId) {
        List<Sentence> sentences = sentenceRepository.findByUserId(userId);
        List<SentenceDtoTable> sentenceDtos = new ArrayList<>();

        for (Sentence sentence : sentences) {
            List<TranslationWithRuleDTO> translations = translationService.getTranslationsBySentenceIdWithRules(sentence.getSentenceId());
            List<Topic> topics = topicService.getTopicsBySentenceId(sentence.getSentenceId());
            SentenceDtoTable sentenceDto = new SentenceDtoTable(sentence, translations, topics);
            sentenceDtos.add(sentenceDto);
        }

        return sentenceDtos;
    }


    public ResponseEntity<Sentence> updateSentence(Long id, SentenceDTO sentenceDto) {
        Optional<Sentence> optionalSentence = sentenceRepository.findById(id);
        if (optionalSentence.isPresent()) {
            Sentence sentence = optionalSentence.get();

            // Update basic fields
            sentence.setUserId(sentenceDto.getUserId());
            sentence.setLearningSentence(sentenceDto.getLearningSentence());
            sentence.setComment(sentenceDto.getComment());
            sentence.setUserLink(sentenceDto.getUserLink());

            // Handle topics
            List<SentenceTopic> sentenceTopics = new ArrayList<>();
            for (Long topicsId : sentenceDto.getTopicsIds()) {
                Topic topic = topicRepository.findById(topicsId)
                        .orElseThrow(() -> new RuntimeException("Topic not found with id: " + topicsId));
                SentenceTopic sentenceTopic = new SentenceTopic();
                sentenceTopic.setSentence(sentence);
                sentenceTopic.setTopic(topic);
                sentenceTopics.add(sentenceTopic);
            }
            sentence.setSentenceTopics(sentenceTopics);

            // Handle rules
            List<SentenceRule> sentenceRules = new ArrayList<>();
            for (Long rulesId : sentenceDto.getRulesIds()) {
                Rule rule = ruleRepository.findById(rulesId)
                        .orElseThrow(() -> new RuntimeException("Rule not found with id: " + rulesId));
                SentenceRule sentenceRule = new SentenceRule();
                sentenceRule.setSentence(sentence);
                sentenceRule.setRule(rule);
                sentenceRules.add(sentenceRule);
            }
            sentence.setSentenceRules(sentenceRules);

            // Save updated sentence
            Sentence updatedSentence = sentenceRepository.save(sentence);
            return new ResponseEntity<>(updatedSentence, HttpStatus.OK);
        } else {
            throw new RuntimeException("Sentence not found with id " + id);
        }
    }

    public ResponseEntity<Void> deleteSentenceByIdAndUserId(Long sentenceId, Long userId) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findBySentenceIdAndUserId(sentenceId, userId);
        if (sentenceOptional.isPresent()) {
            sentenceRepository.delete(sentenceOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}