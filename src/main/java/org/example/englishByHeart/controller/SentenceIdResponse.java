package org.example.englishByHeart.controller;

public class SentenceIdResponse {
    private Long sentenceId;

    public SentenceIdResponse(Long sentenceId) {
    }

    public Long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Long sentenceId) {
        this.sentenceId = sentenceId;
    }
}
