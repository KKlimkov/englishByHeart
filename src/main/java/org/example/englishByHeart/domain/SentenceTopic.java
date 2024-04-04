package org.example.englishByHeart.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "sentence_topic")
public class SentenceTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}