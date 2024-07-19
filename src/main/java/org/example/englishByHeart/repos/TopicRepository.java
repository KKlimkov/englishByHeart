package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    // You can define additional query methods here if needed
    Optional<Topic> findById(long id);
    List<Topic> findByUserId(Long userId);
    List<Topic> findByTopicIdIn(List<Long> topicIds);
    Set<Topic> findAllByTopicIdIn(Collection<Long> ids);
}