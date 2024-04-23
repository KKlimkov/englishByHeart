package org.example.englishByHeart.dto;

import java.util.List;

public class ExercisesStartDTO {
    public String learningSentence;
    public String comment;
    public List<TranslationWithRuleDTO> translations;

    public String getLearningSentence() {
        return learningSentence;
    }

    public void setLearningSentence(String learningSentence) {
        this.learningSentence = learningSentence;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TranslationWithRuleDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationWithRuleDTO> translations) {
        this.translations = translations;
    }
}
