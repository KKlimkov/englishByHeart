package org.example.englishByHeart.dto;

public class TopicDTO {
    private Long userId;
    private String topicName;
    private Long topicId;

    public TopicDTO(Long topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
