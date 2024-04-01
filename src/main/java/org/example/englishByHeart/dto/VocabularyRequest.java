package org.example.englishByHeart.dto;

import java.util.List;

public class VocabularyRequest {

    private Long userId;
    private String learningSentence;
    private String comment;
    private Long userLink;
    private Long topicId;
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

    public Long getUserLink() {
        return userLink;
    }

    public void setUserLink(Long userLink) {
        this.userLink = userLink;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }


    public List<TranslationRequestForAdd> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationRequestForAdd> translations) {
        this.translations = translations;
    }
}