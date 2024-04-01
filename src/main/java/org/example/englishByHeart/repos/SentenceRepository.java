package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    // Add custom query methods if needed
}