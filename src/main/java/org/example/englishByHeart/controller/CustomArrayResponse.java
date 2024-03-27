package org.example.englishByHeart.controller;

import java.util.List;

public class CustomArrayResponse<T> {

    private boolean success;
    private String errorMessage;
    private List<T> data;

    // Constructors, getters, and setters


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return (T) data;
    }

    public void setData(List<Integer> data) {
        this.data = (List<T>) data;
    }
}
