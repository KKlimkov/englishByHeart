package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long exerciseId;

    private Long currentSentenceId;

    private Long userId;

    private String exerciseName;

    @Column(columnDefinition = "text[]")
    private String[] sentencesId;

    @Column(columnDefinition = "text[]")
    private String[] currentSentencesId;

    @Column(columnDefinition = "text[]")
    private String[] topicsIds;

    @Column(columnDefinition = "text[]")
    private String[] rulesIds;
    // Getters and setters

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updateDate;

    private boolean hasChanged;

    private boolean isActive;

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

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String sentenceName) {
        this.exerciseName = sentenceName;
    }

    public String[] getSentencesId() {
        return sentencesId;
    }

    public void setSentencesId(String[] sentencesId) {
        this.sentencesId = sentencesId;
    }

    public String[] getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(String[] topicsIds) {
        this.topicsIds = topicsIds;
    }

    public boolean isHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    public String[] getRulesIds() {
        return rulesIds;
    }

    public void setRulesIds(String[] rulesIds) {
        this.rulesIds = rulesIds;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getCurrentSentenceId() {
        return currentSentenceId;
    }

    public void setCurrentSentenceId(Long currentSentenceId) {
        this.currentSentenceId = currentSentenceId;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }
}