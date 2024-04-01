package org.example.englishByHeart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private List<TranslationRuleLink> translationRuleLinks = new ArrayList<>();

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

    public List<TranslationRuleLink> getTranslationRuleLinks() {
        return translationRuleLinks;
    }

    public void setTranslationRuleLinks(List<TranslationRuleLink> translationRuleLinks) {
        this.translationRuleLinks = translationRuleLinks;
    }
}