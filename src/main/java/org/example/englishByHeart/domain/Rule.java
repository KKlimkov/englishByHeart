package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long ruleId;

    private String rule;
    private String link;

    private Long userId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updateDate;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TranslationRule> translationRule = new ArrayList<>();

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SentenceRule> sentenceRules = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public List<SentenceRule> getSentenceRules() {
        return sentenceRules;
    }

    public void setSentenceRules(List<SentenceRule> sentenceRules) {
        this.sentenceRules = sentenceRules;
    }

    public List<TranslationRule> getTranslationRule() {
        return translationRule;
    }

    public void setTranslationRule(List<TranslationRule> translationRule) {
        this.translationRule = translationRule;
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