package org.example.englishByHeart.repos;

import jakarta.transaction.Transactional;
import org.example.englishByHeart.domain.Exercise;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {


    @Query(value = "SELECT current_sentences_id FROM exercise WHERE user_id = :userId", nativeQuery = true)
    String getCurrentSentencesIdArrayByUserId(@Param("userId") Long userId);
    List<Exercise> findByUserId(Long userId, Sort sort);

    @Query("SELECT e FROM Exercise e WHERE (:userId IS NULL OR e.userId = :userId) AND (:exerciseId IS NULL OR e.exerciseId = :exerciseId)")
    List<Exercise> findByUserIdAndExerciseId(
            @Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId
    );

    @Query("SELECT e FROM Exercise e WHERE (:userId IS NULL OR e.userId = :userId) AND (:exerciseId IS NULL OR e.exerciseId = :exerciseId) AND (:isActive IS NULL OR e.isActive = :isActive)")
    List<Exercise> findByUserIdAndExerciseIdAndActive(
            @Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId,
            @Param("isActive") Boolean isActive,
            Sort sort
    );

    @Query("SELECT e FROM Exercise e JOIN e.topics t WHERE (:userId IS NULL OR e.userId = :userId) AND (:exerciseId IS NULL OR e.exerciseId = :exerciseId) AND (:isActive IS NULL OR e.isActive = :isActive) ORDER BY t.name ASC")
    List<Exercise> findByUserIdAndExerciseIdAndActiveOrderByTopicName(Long userId, Long exerciseId, Boolean isActive);

    @Query("SELECT e FROM Exercise e JOIN e.rules r WHERE (:userId IS NULL OR e.userId = :userId) AND (:exerciseId IS NULL OR e.exerciseId = :exerciseId) AND (:isActive IS NULL OR e.isActive = :isActive) ORDER BY r.name ASC")
    List<Exercise> findByUserIdAndExerciseIdAndActiveOrderByRuleName(Long userId, Long exerciseId, Boolean isActive);

    @Modifying
    @Transactional
    @Query("UPDATE Exercise e SET e.isActive = false WHERE e.userId = :userId")
    int deactivateAllExercisesByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Exercise e SET e.isActive = true WHERE e.exerciseId = :exerciseId")
    int activateExerciseById(Long exerciseId);

    @Query("SELECT e.exerciseId FROM Exercise e WHERE e.isActive = true AND e.userId = ?1")
    Optional<Long> findActiveExerciseIdByUserId(Long userId);
}