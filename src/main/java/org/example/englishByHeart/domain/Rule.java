package org.example.englishByHeart.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long ruleId;

    private String rule;
    private String link;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranslationRuleLink> translationRuleLinks = new ArrayList<>();

    // Constructors, getters, setters


    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
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

    public List<TranslationRuleLink> getTranslationRuleLinks() {
        return translationRuleLinks;
    }

    public void setTranslationRuleLinks(List<TranslationRuleLink> translationRuleLinks) {
        this.translationRuleLinks = translationRuleLinks;
    }
}