package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
}