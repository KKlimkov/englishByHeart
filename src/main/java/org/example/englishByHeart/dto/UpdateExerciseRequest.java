package org.example.englishByHeart.dto;

import java.util.List;

public class UpdateExerciseRequest {

    private Long userId;
    private Long currentSentenceId;
    private String exerciseName;
    private List<Long> sentencesId;
    private List<Long> currentSentencesId;
    private List<Long> topicsIds;
    private List<Long> rulesIds;
    private boolean hasChanged;
    private boolean isActive;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public List<Long> getSentencesId() {
        return sentencesId;
    }

    public void setSentencesId(List<Long> sentencesId) {
        this.sentencesId = sentencesId;
    }

    public List<Long> getCurrentSentencesId() {
        return currentSentencesId;
    }

    public void setCurrentSentencesId(List<Long> currentSentencesId) {
        this.currentSentencesId = currentSentencesId;
    }

    public List<Long> getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(List<Long> topicsIds) {
        this.topicsIds = topicsIds;
    }

    public List<Long> getRulesIds() {
        return rulesIds;
    }

    public void setRulesIds(List<Long> rulesIds) {
        this.rulesIds = rulesIds;
    }

    public boolean getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
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
}
