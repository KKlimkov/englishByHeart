package org.example.englishByHeart.dto;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Translation;

import java.util.List;

public class SentenceDtoTable {
    private Long sentenceId;
    private Long userId;
    private String learningSentence;
    private String comment;
    private String userLink;
    private List<Translation> translations;

    // Constructor
    public SentenceDtoTable(Sentence sentence, List<Translation> translations) {
        this.sentenceId = sentence.getSentenceId();
        this.userId = sentence.getUserId();
        this.learningSentence = sentence.getLearningSentence();
        this.comment = sentence.getComment();
        this.userLink = sentence.getUserLink();
        this.translations = translations;
    }

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
}
