package org.example.englishByHeart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickedElementResponseDTO {
    private List<Long> modifiedArray;
    private Long pickedElement;

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
