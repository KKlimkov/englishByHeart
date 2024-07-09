package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
    List<Translation> findBySentenceIdIn(List<Long> sentenceIds);
    List<Translation> findBySentenceId(Long sentenceId);
}