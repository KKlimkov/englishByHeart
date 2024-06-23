package org.example.englishByHeart.dto;

import org.example.englishByHeart.domain.Translation;

import java.util.List;

public class Lesson {
    private Long sentenceId;
    private Long userId;
    private String learningSentence;
    private String comment;
    private String userLink;
    private List<Translation> translations;
    private List<Long> modifiedArray;

    // Getters and setters...

    public Long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Long sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLearningSentence() {
        return learningSentence;
    }

    public void setLearningSentence(String learningSentence) {
        this.learningSentence = learningSentence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public List<Long> getModifiedArray() {
        return modifiedArray;
    }

    public void setModifiedArray(List<Long> modifiedArray) {
        this.modifiedArray = modifiedArray;
    }
}
