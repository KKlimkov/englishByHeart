package org.example.englishByHeart.dto;

import java.util.List;

public class CreateExerciseRequest {
    private Long userId;
    private String exerciseName;

    private List<Long> sentencesId;

    private List<Long> currentSentencesId;

    private List<Long> topicsIds;

    private List<Long> rulesIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String sentenceName) {
        this.exerciseName = sentenceName;
    }

    public List<Long> getSentencesId() {
        return sentencesId;
    }

    public void setSentencesId(List<Long> sentencesId) {
        this.sentencesId = sentencesId;
    }

    public List<Long> getRulesIds() {
        return rulesIds;
    }

    public void setRulesIds(List<Long> rulesIds) {
        this.rulesIds = rulesIds;
    }

    public List<Long> getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(List<Long> topicsIds) {
        this.topicsIds = topicsIds;
    }

    public List<Long> getCurrentSentencesId() {
        return currentSentencesId;
    }

    public void setCurrentSentencesId(List<Long> currentSentencesId) {
        this.currentSentencesId = currentSentencesId;
    }
}
