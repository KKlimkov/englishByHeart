package org.example.englishByHeart.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "sentence_rule")
public class SentenceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    // Default constructor
    public SentenceRule() {
    }

    // Constructor with parameters
    public SentenceRule(Sentence sentence, Rule rule) {
        this.sentence = sentence;
        this.rule = rule;
    }


    // Constructors, getters, and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }


    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}