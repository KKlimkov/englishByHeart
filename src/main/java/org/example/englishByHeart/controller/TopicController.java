package org.example.englishByHeart.controller;

import org.example.englishByHeart.service.TopicService;
import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    public List<Topic> getAllTopicsByUser(Long userId) {
        return topicService.getAllTopicsByUser(userId);
    }


    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setUserId(topicDTO.getUserId());
        topic.setTopicName(topicDTO.getTopicName());
        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }
}