package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.SentenceTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceTopicRepository extends JpaRepository<SentenceTopic, Long> {
    List<SentenceTopic> findBySentence_SentenceId(Long sentenceId);
}