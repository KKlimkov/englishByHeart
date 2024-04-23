package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long exerciseId;

    private Long userId;

    @Column(columnDefinition = "text[]")
    private String[] currentSentencesId;
    // Getters and setters


    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String[] getCurrentSentencesId() {
        return currentSentencesId;
    }

    public void setCurrentSentencesId(String[] currentSentencesId) {
        this.currentSentencesId = currentSentencesId;
    }


}