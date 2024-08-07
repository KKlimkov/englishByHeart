package org.example.englishByHeart.dto;

import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.domain.Translation;

import java.time.ZonedDateTime;
import java.util.List;

public class SentenceDtoTable {
    private Long sentenceId;
    private Long userId;
    private String learningSentence;
    private String comment;
    private String userLink;
    private List<TranslationWithRuleDTO> translations;
    private List<Topic> topics; // Add this line
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;

    // Constructor
    public SentenceDtoTable(Sentence sentence, List<TranslationWithRuleDTO> translations, List<Topic> topics) {
        this.sentenceId = sentence.getSentenceId();
        this.userId = sentence.getUserId();
        this.learningSentence = sentence.getLearningSentence();
        this.comment = sentence.getComment();
        this.userLink = sentence.getUserLink();
        this.createDate = sentence.getCreateDate();
        this.updateDate = sentence.getUpdateDate();
        this.translations = translations;
        this.topics = topics; // Add this line
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

    public List<TranslationWithRuleDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationWithRuleDTO> translations) {
        this.translations = translations;
    }

    public List<Topic> getTopics() { // Add this getter
        return topics;
    }

    public void setTopics(List<Topic> topics) { // Add this setter
        this.topics = topics;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
