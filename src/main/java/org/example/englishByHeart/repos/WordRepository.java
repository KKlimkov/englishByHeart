package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    // Add custom query methods if needed
}