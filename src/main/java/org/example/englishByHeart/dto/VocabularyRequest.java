package org.example.englishByHeart.dto;

import java.util.List;

public class VocabularyRequest {

    private Long userId;
    private String learningSentence;
    private String comment;
    private String userLink;
    private List<Long> topicsIds;

    private List<TranslationRequestForAdd> translations;

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

    public List<Long> getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(List<Long> topicsIds) {
        this.topicsIds = topicsIds;
    }

    public List<TranslationRequestForAdd> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationRequestForAdd> translations) {
        this.translations = translations;
    }
}