package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.TranslationRule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TranslationRuleRepository extends CrudRepository<TranslationRule, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM TranslationRule tr WHERE tr.translation.id = :translationId")
    void deleteByTranslationId(Long translationId);
}