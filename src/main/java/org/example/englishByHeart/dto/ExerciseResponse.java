package org.example.englishByHeart.dto;

import java.util.List;

public class ExerciseResponse {
    private Long exerciseId;
    private String exerciseName;
    private List<String> currentTopicsIds;
    private List<String> currentRulesIds;

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

    public List<String> getCurrentTopicsIds() {
        return currentTopicsIds;
    }

    public void setCurrentTopicsIds(List<String> currentTopicsIds) {
        this.currentTopicsIds = currentTopicsIds;
    }

    public List<String> getCurrentRulesIds() {
        return currentRulesIds;
    }

    public void setCurrentRulesIds(List<String> currentRulesIds) {
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
