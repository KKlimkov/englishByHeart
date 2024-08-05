package org.example.englishByHeart.enums;

public enum SortBy {
    CREATE_DATE("createDate"),
    UPDATE_DATE("updateDate"),
    RULE_NAME("rule"),
    RULE_LINK("link"),
    TOPIC_NAME("topicName"),
    LEARNING_SENTENCE("learningSentence");

    private final String field;

    SortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}