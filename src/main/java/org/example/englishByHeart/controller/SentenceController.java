package org.example.englishByHeart.controller;

import org.example.englishByHeart.dto.SentenceIdResponse;
import org.example.englishByHeart.service.SentenceService;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.dto.SentenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/sentence")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<SentenceIdResponse>> createSentence(@RequestBody SentenceDTO sentence) {
        CustomResponse<SentenceIdResponse> response = sentenceService.createSentence(sentence);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/search")
    public List<Sentence> searchSentences(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Set<Long> sentenceIds, // Change List to Set
            @RequestParam(required = false) List<Long> sentenceTopics) {
        return sentenceService.searchSentences(userId, sentenceIds, sentenceTopics);
    }

    @GetMapping("/byRule/{ruleId}")
    public List<Sentence> getSentencesByRule(@PathVariable Long ruleId) {
        return sentenceService.getSentencesByRule(ruleId);
    }

    @GetMapping("/byRules/{ruleIds}")
    public List<Sentence> getSentencesByRules(@PathVariable List<Long> ruleIds) {
        return sentenceService.getSentencesByRules(ruleIds);
    }

    @GetMapping("/byTopic/{topicId}")
    public List<Sentence> getSentencesByTopicId(@PathVariable Long topicId) {
        return sentenceService.getSentencesByTopicId(topicId);
    }

    @GetMapping("/byTopics/{topicIds}")
    public List<Sentence> getSentencesByTopicIds(@PathVariable List<Long> topicIds) {
        return sentenceService.getSentencesByTopicIds(topicIds);
    }

    @GetMapping("/sentencesByTopicsAndRules")
    public List<Sentence> getSentencesByTopicsAndRules(
            @RequestParam(required = false) List<Long> topicIds,
            @RequestParam(required = false) List<Long> ruleIds) {

        if (topicIds != null && ruleIds == null) {
            // Handle case where only topicIds are provided
            return sentenceService.getSentencesByTopicIds(topicIds);
        }

        if (topicIds == null && ruleIds != null) {
            // Handle case where only ruleIds are provided
            return sentenceService.getSentencesByRules(ruleIds);
        }

        // Handle case where both topicIds and ruleIds are provided
        return sentenceService.getSentencesByTopicsAndRules(topicIds, ruleIds);
    }


    @GetMapping("/sentencesIdsByTopicsAndRules")
    public Set<Long> getSentenceIdsByTopicsAndRules(
            @RequestParam(required = false) List<Long> topicIds,
            @RequestParam(required = false) List<Long> ruleIds) {

        return sentenceService.getSentenceIdsByTopicsAndRules(topicIds, ruleIds);
    }


    @GetMapping("/sentenceById/{sentenceId}")
    public Sentence getSentenceBySentenceId(@PathVariable Long sentenceId) {
        return sentenceService.getSentenceBySentenceId(sentenceId);
    }

}
