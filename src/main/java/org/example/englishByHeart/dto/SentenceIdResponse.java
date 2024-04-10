package org.example.englishByHeart.dto;

public class SentenceIdResponse {
    private Long sentenceId;

    public SentenceIdResponse(Long sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Long sentenceId) {
        this.sentenceId = sentenceId;
    }
}
