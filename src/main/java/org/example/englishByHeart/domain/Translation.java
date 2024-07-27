package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "translations")
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long translateId;

    private String translation;

    private Long sentenceId;

    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TranslationRule> translationRuleLinks = new ArrayList<>();

    @Transient
    private List<RuleAndLink> rulesAndLinks;
    // Constructors, getters, setters

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

    public List<TranslationRule> getTranslationRuleLinks() {
        return translationRuleLinks;
    }

    public void setTranslationRuleLinks(List<TranslationRule> translationRuleLinks) {
        this.translationRuleLinks = translationRuleLinks;
    }

    public List<RuleAndLink> getRulesAndLinks() {
        return rulesAndLinks;
    }

    public void setRulesAndLinks(List<RuleAndLink> rulesAndLinks) {
        this.rulesAndLinks = rulesAndLinks;
    }

}

class RuleAndLink {

    private String ruleId;
    private String rule;
    private String link;

    public RuleAndLink() {
    }

    public RuleAndLink(String rule, String link) {
        this.rule = rule;
        this.link = link;
    }

    public String getRule() {
        return rule;
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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}