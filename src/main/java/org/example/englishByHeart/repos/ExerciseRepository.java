package org.example.englishByHeart.repos;

import org.example.englishByHeart.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query(value = "SELECT current_sentences_id FROM exercise WHERE user_id = :userId", nativeQuery = true)
    String getCurrentSentencesIdArrayByUserId(@Param("userId") Long userId);
    List<Exercise> findByUserId(Long userId);

    @Query("SELECT e FROM Exercise e WHERE (:userId IS NULL OR e.userId = :userId) AND (:exerciseId IS NULL OR e.exerciseId = :exerciseId)")
    List<Exercise> findByUserIdAndExerciseId(
            @Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId
    );

}