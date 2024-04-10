package org.example.englishByHeart.dto;

public class RuleDTO {

    private String rule;
    private String link;
    private Long userId;

    // Constructors, getters, setters

    public String getRule() {
        return rule;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
