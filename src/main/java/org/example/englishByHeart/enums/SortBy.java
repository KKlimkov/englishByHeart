package org.example.englishByHeart.enums;

public enum SortBy {
    CREATE_DATE("createDate"),
    UPDATE_DATE("updateDate"),
    NAME("rule"),
    LINK("link");

    private final String field;

    SortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}