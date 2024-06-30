package org.example.englishByHeart.dto;

import java.util.List;

public class UpdateExerciseRequestForUpdate {

    private Long userId;
    private Long currentSentenceId;
    private List<Long> currentSentencesId;

    // Getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrentSentenceId() {
        return currentSentenceId;
    }

    public void setCurrentSentenceId(Long currentSentenceId) {
        this.currentSentenceId = currentSentenceId;
    }

    public List<Long> getCurrentSentencesId() {
        return currentSentencesId;
    }

    public void setCurrentSentencesId(List<Long> currentSentencesId) {
        this.currentSentencesId = currentSentencesId;
    }

}
