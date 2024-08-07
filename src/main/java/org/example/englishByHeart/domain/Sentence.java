package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.*;

@Entity
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long sentenceId;

    private Long userId;
    private String learningSentence;
    private String comment;
    private String userLink;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updateDate;

    @OneToMany(mappedBy = "sentence",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SentenceTopic> sentenceTopics = new ArrayList<>();

    @OneToMany(mappedBy = "sentence",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SentenceRule> sentenceRules = new ArrayList<>();

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

    public List<SentenceTopic> getSentenceTopics() {
        return sentenceTopics;
    }

    public void setSentenceTopics(List<SentenceTopic> sentenceTopics) {
        this.sentenceTopics = sentenceTopics;
    }

    public List<SentenceRule> getSentenceRules() {
        return sentenceRules;
    }

    public void setSentenceRules(List<SentenceRule> sentenceRules) {
        this.sentenceRules = sentenceRules;
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