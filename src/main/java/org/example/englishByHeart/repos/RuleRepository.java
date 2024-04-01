package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    Optional<Rule> findById(long id);
}