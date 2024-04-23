package org.example.englishByHeart.dto;

import java.util.List;

public class CreateExerciseRequest {
    private Long userId;
    private List<Long> currentSentencesId;

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
}
