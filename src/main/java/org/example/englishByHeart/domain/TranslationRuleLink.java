package org.example.englishByHeart.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "translation_rule_link")
public class TranslationRuleLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "translate_id")
    private Translation translation;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    // Constructors, getters, setters
}