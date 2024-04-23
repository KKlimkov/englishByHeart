package org.example.englishByHeart.dto;

import java.util.List;

public class TranslationWithRuleDTO {
    private Long translateId;
    private String translation;
    private Long sentenceId;
    private List<RulesAndLinks> rulesAndLinks;

    public Long getTranslateId() {
        return translateId;
    }

    public void setTranslateId(Long translateId) {
        this.translateId = translateId;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(Long sentenceId) {
        this.sentenceId = sentenceId;
    }

    public List<RulesAndLinks> getRulesAndLinks() {
        return rulesAndLinks;
    }

    public void setRulesAndLinks(List<RulesAndLinks> rulesAndLinks) {
        this.rulesAndLinks = rulesAndLinks;
    }

}