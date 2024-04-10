package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.dto.TopicDTO;
import org.example.englishByHeart.repos.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

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


    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
