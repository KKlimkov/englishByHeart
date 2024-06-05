package org.example.englishByHeart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateExercisePayload {
    @NotNull(message = "userId is missing or invalid")
    private Long userId;

    @NotEmpty(message = "sentenceName is missing")
    private String sentenceName;

    private List<Long> topicIds;

    private List<Long> ruleIds;

    // Getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Long> topicIds) {
        this.topicIds = topicIds;
    }

    public List<Long> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<Long> ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String getSentenceName() {
        return sentenceName;
    }

    public void setSentenceName(String sentenceName) {
        this.sentenceName = sentenceName;
    }
}

