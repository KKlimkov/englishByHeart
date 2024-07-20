package org.example.englishByHeart.repos;

import jakarta.transaction.Transactional;
import org.example.englishByHeart.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
    List<Translation> findBySentenceIdIn(List<Long> sentenceIds);
    List<Translation> findBySentenceId(Long sentenceId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Translation t WHERE t.sentenceId = :sentenceId")
    void deleteBySentenceId(Long sentenceId);

}