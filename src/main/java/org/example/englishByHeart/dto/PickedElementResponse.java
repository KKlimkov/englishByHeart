package org.example.englishByHeart.dto;

import java.util.List;

public class PickedElementResponse {
    private List<Long> modifiedArray;
    private Long pickedElement;

    public PickedElementResponse() {
        // Default constructor needed by Jackson
    }
    // Getters and setters

    public List<Long> getModifiedArray() {
        return modifiedArray;
    }

    public void setModifiedArray(List<Long> modifiedArray) {
        this.modifiedArray = modifiedArray;
    }

    public Long getPickedElement() {
        return pickedElement;
    }

    public void setPickedElement(Long pickedElement) {
        this.pickedElement = pickedElement;
    }
}
