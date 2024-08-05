package org.example.englishByHeart.controller;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.enums.SortBy;
import org.example.englishByHeart.service.TopicService;
import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    public List<Topic> getAllTopicsByUser(@RequestParam Long userId,
                                          @RequestParam(required = false) SortBy sortBy,
                                          @RequestParam(required = false, defaultValue = "ASC") Sort.Direction mode) {
        return topicService.getAllTopicsByUser(userId, sortBy, mode);
    }


    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setUserId(topicDTO.getUserId());
        topic.setTopicName(topicDTO.getTopicName());
        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }

    @GetMapping("/getTopicsByTopicsIds")
    public List<Topic> getTopicsByTopicIds(@RequestParam List<Long> topicsIds) {
        return topicService.getTopicsByTopicIds(topicsIds);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopicName(@PathVariable Long id, @RequestParam String newTopicName) {
        Topic updatedTopic = topicService.updateTopicName(id, newTopicName);
        return ResponseEntity.ok(updatedTopic);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}