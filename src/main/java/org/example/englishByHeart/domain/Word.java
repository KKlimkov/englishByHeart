package org.example.englishByHeart.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wordId;
    private Integer userId;
    private Integer sentenceId;
    private Integer learningWord;
    private Integer translateId;
    private String comment;
    private Integer linkId;
    private String picture;

    // Getters and setters

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Integer sentenceId) {
        this.sentenceId = sentenceId;
    }

    public Integer getLearningWord() {
        return learningWord;
    }

    public void setLearningWord(Integer learningWord) {
        this.learningWord = learningWord;
    }

    public Integer getTranslateId() {
        return translateId;
    }

    public void setTranslateId(Integer translateId) {
        this.translateId = translateId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
