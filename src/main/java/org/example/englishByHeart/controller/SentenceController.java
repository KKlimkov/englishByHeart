package org.example.englishByHeart.controller;

import org.example.englishByHeart.dto.SentenceIdResponse;
import org.example.englishByHeart.service.SentenceService;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.dto.SentenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/sentence")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @PostMapping
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

}
