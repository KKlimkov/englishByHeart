package org.example.englishByHeart.dto;

import java.util.List;

public class CreateExerciseRequest {
    private Long userId;
    private String sentenceName;

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

    public String getSentenceName() {
        return sentenceName;
    }

    public void setSentenceName(String sentenceName) {
        this.sentenceName = sentenceName;
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
