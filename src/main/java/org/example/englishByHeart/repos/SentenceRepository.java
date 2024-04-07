package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findByUserId(Long userId);

    List<Sentence> findBySentenceIdIn(Set<Long> sentenceIds);

    List<Sentence> findBySentenceTopicsIn(List<Long> sentenceTopics);

    List<Sentence> findByUserIdAndSentenceId(Long userId, Long sentenceId);

    List<Sentence> findByUserIdAndSentenceIdIn(Long userId, Set<Long> sentenceIds);

    List<Sentence> findByUserIdAndSentenceTopicsIn(Long userId, List<Long> sentenceTopics);

    @Query("SELECT s FROM Sentence s WHERE s.userId = :userId AND s.sentenceId IN :sentenceIds AND s.sentenceTopics IN :sentenceTopics")
    List<Sentence> findByUserIdAndSentenceIdInAndSentenceTopics(Long userId, Set<Long> sentenceIds, List<Long> sentenceTopics);
}