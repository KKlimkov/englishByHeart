package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Rule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    Optional<Rule> findById(long id);

    @Query("SELECT r FROM Rule r WHERE r.userId = :userId ORDER BY r.createDate")
    List<Rule> findAllByUserIdOrderByCreateDate(@Param("userId") Long userId);

    @Query("SELECT r FROM Rule r WHERE r.userId = :userId ORDER BY r.updateDate")
    List<Rule> findAllByUserIdOrderByUpdateDate(@Param("userId") Long userId);

    @Query("SELECT r FROM Rule r WHERE r.userId = :userId")
    List<Rule> findAllByUserId(@Param("userId") Long userId, Sort sort);

    @Query("SELECT tr.rule FROM TranslationRule tr WHERE tr.translation.sentenceId = :sentenceId")
    List<Rule> findRulesBySentenceId(@Param("sentenceId") Long sentenceId);

    List<Rule> findByRuleIdIn(List<Long> ruleIds);

    Set<Rule> findAllByRuleIdIn(Collection<Long> ids);

}