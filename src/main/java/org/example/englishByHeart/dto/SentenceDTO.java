package org.example.englishByHeart.dto;

import java.util.List;

public class SentenceDTO {

    private Long userId;
    private String learningSentence;
    private String comment;
    private Long userLink;

    private List<Long> topicsIds;

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

    public List<Long> getTopicsIds() {
        return topicsIds;
    }

    public void setTopicsIds(List<Long> topicsIds) {
        this.topicsIds = topicsIds;
    }
}
