package org.example.englishByHeart.dto;

import java.util.List;

public class ExerciseResponse {
    private Long exerciseId;
    private String exerciseName;

    private List<IdNamePair> currentTopicsIds;
    private List<IdNamePair> currentRulesIds;

    private List<String> currentSentencesIds;
    private Long currentSentenceId;

    private Long numberOfSentences;

    // Getters and Setters
    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public List<IdNamePair> getCurrentTopicsIds() {
        return currentTopicsIds;
    }

    public void setCurrentTopicsIds(List<IdNamePair> currentTopicsIds) {
        this.currentTopicsIds = currentTopicsIds;
    }

    public List<IdNamePair> getCurrentRulesIds() {
        return currentRulesIds;
    }

    public void setCurrentRulesIds(List<IdNamePair> currentRulesIds) {
        this.currentRulesIds = currentRulesIds;
    }

    public Long getNumberOfSentences() {
        return numberOfSentences;
    }

    public void setNumberOfSentences(Long numberOfSentences) {
        this.numberOfSentences = numberOfSentences;
    }

    public List<String> getCurrentSentencesIds() {
        return currentSentencesIds;
    }

    public void setCurrentSentencesIds(List<String> currentSentencesIds) {
        this.currentSentencesIds = currentSentencesIds;
    }

    public Long getCurrentSentenceId() {
        return currentSentenceId;
    }

    public void setCurrentSentenceId(Long currentSentenceId) {
        this.currentSentenceId = currentSentenceId;
    }
}
