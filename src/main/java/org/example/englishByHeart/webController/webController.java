package org.example.englishByHeart.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class webController {

    @GetMapping("/")
    public String common() {
        return "common";
    }

    @GetMapping("/rules-form")
    public String showRuleForm() {
        return "rules-form";
    }

    @GetMapping("/topics-form")
    public String showTopicForm() {
        return "topics-form";
    }

    @GetMapping("/sentences-form")
    public String showSentencesForm() {
        return "sentences-form";
    }

    @GetMapping("/exercises-form")
    public String showExerciseForm() {
        return "exercises-form";
    }

    @GetMapping("/lesson-form")
    public String showLessonForm() {
        return "lesson-form";
    }

    @PostMapping("/lesson-form")
    public String showLessonForm(@RequestParam String learningSentence) {
        return "lesson-form";
    }

    // Add mappings for Rules, Topics, Sentences, and Exercises as needed
}