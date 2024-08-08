package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Sentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {

    @Query("SELECT r FROM Sentence r WHERE r.userId = :userId")
    Page<Sentence> findByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<Sentence> findBySentenceId(Long sentenceId);

    List<Sentence> findBySentenceIdIn(Set<Long> sentenceIds);

    List<Sentence> findBySentenceTopicsIn(List<Long> sentenceTopics);

    List<Sentence> findByUserIdAndSentenceId(Long userId, Long sentenceId);

    List<Sentence> findByUserIdAndSentenceIdIn(Long userId, Set<Long> sentenceIds);

    List<Sentence> findByUserIdAndSentenceTopicsIn(Long userId, List<Long> sentenceTopics);

    @Query("SELECT s FROM Sentence s WHERE s.userId = :userId AND s.sentenceId IN :sentenceIds AND s.sentenceTopics IN :sentenceTopics")
    List<Sentence> findByUserIdAndSentenceIdInAndSentenceTopics(Long userId, Set<Long> sentenceIds, List<Long> sentenceTopics);

    @Query("SELECT sr.sentence FROM SentenceRule sr WHERE sr.rule.ruleId = :ruleId")
    List<Sentence> findByRuleId(@Param("ruleId") Long ruleId);

    @Query("SELECT DISTINCT s FROM Sentence s JOIN s.sentenceRules sr WHERE sr.rule.ruleId IN :ruleIds")
    List<Sentence> findByRuleIds(@Param("ruleIds") List<Long> ruleIds);

    @Query("SELECT DISTINCT s FROM Sentence s JOIN s.sentenceTopics st WHERE st.topic.topicId = :topicId")
    List<Sentence> findByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT DISTINCT s FROM Sentence s JOIN s.sentenceTopics st WHERE st.topic.topicId IN :topicIds")
    List<Sentence> findByTopicIds(@Param("topicIds") List<Long> topicIds);


    @Query("SELECT DISTINCT s FROM Sentence s " +
            "JOIN s.sentenceTopics st " +
            "JOIN s.sentenceRules sr " +
            "WHERE st.topic.topicId IN :topicIds AND sr.rule.ruleId IN :ruleIds")
    List<Sentence> findByTopicsAndRules(@Param("topicIds") List<Long> topicIds, @Param("ruleIds") List<Long> ruleIds);


    @Query("SELECT DISTINCT s.sentenceId FROM Sentence s " +
            "JOIN s.sentenceTopics st " +
            "JOIN s.sentenceRules sr " +
            "WHERE st.topic.topicId IN :topicIds AND sr.rule.ruleId IN :ruleIds")
    Set<Long> findSentenceIdsByTopicsAndRules(@Param("topicIds") List<Long> topicIds, @Param("ruleIds") List<Long> ruleIds);

    @Query("SELECT DISTINCT s.sentenceId FROM Sentence s " +
            "JOIN s.sentenceTopics st " +
            "WHERE st.topic.topicId IN :topicIds")
    Set<Long> findSentenceIdsByTopicIds(@Param("topicIds") List<Long> topicIds);

    @Query("SELECT DISTINCT s.sentenceId FROM Sentence s " +
            "JOIN s.sentenceRules sr " +
            "WHERE sr.rule.ruleId IN :ruleIds")
    Set<Long> findSentenceIdsByRuleIds(@Param("ruleIds") List<Long> ruleIds);

    Optional<Sentence> findBySentenceIdAndUserId(Long sentenceId, Long userId);

}