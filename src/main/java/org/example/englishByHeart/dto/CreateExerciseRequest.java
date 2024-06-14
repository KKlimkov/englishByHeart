package org.example.englishByHeart.dto;

import java.util.List;

public class CreateExerciseRequest {
    private Long userId;
    private String exerciseName;

    private List<Long> currentSentencesId;

    private List<Long> currentTopicsIds;

    private List<Long> currentRulesIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCurrentSentencesId() {
        return currentSentencesId;
    }

    public void setCurrentSentencesId(List<Long> currentSentencesId) {
        this.currentSentencesId = currentSentencesId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String sentenceName) {
        this.exerciseName = sentenceName;
    }

    public List<Long> getCurrentTopicsIds() {
        return currentTopicsIds;
    }

    public void setCurrentTopicsIds(List<Long> currentTopicsIds) {
        this.currentTopicsIds = currentTopicsIds;
    }

    public List<Long> getCurrentRulesIds() {
        return currentRulesIds;
    }

    public void setCurrentRulesIds(List<Long> currentRulesIds) {
        this.currentRulesIds = currentRulesIds;
    }
}
