package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.SentenceTopic;
import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.dto.TopicDTO;
import org.example.englishByHeart.repos.SentenceTopicRepository;
import org.example.englishByHeart.repos.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SentenceTopicRepository sentenceTopicRepository;

    public List<TopicDTO> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(topic -> new TopicDTO(topic.getTopicId(), topic.getTopicName()))
                .collect(Collectors.toList());
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public List<Topic> getAllTopicsByUser(Long userId) {
        List<Topic> topics = topicRepository.findByUserId(userId);
        return topics;
    }

    public List<Topic> getTopicsByTopicIds(List<Long> topicsIds) {
        return topicRepository.findAllById(topicsIds);
    }

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic updateTopicName(Long topicId, String newTopicName) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
        topic.setTopicName(newTopicName);
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
        topicRepository.delete(topic);
    }

    // New method to get topics by sentence ID
    public List<Topic> getTopicsBySentenceId(Long sentenceId) {
        List<SentenceTopic> sentenceTopics = sentenceTopicRepository.findBySentence_SentenceId(sentenceId);
        return sentenceTopics.stream()
                .map(SentenceTopic::getTopic)
                .collect(Collectors.toList());
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Other exception handlers can be added here
}
