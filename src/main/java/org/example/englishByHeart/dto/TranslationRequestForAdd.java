package org.example.englishByHeart.dto;

import java.util.List;

public class TranslationRequestForAdd {
    private String translation;
    private List<Long> ruleIds;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public List<Long> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<Long> ruleIds) {
        this.ruleIds = ruleIds;
    }

    // Getters and setters
}