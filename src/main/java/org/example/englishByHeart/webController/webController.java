package org.example.englishByHeart.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class webController {

    @GetMapping("/")
    public String common() {
        return "common";
    }

    @GetMapping("/rules-form")
    public String showRuleForm() {
        return "rules-form"; // Assuming the template is named "rules-form.ftl"
    }

    @GetMapping("/topics-form")
    public String showTopicForm() {
        return "topics-form"; // Assuming the template is named "rules-form.ftl"
    }

    @GetMapping("/sentences-form")
    public String showSentencesForm() {
        return "sentences-form"; // Assuming the template is named "rules-form.ftl"
    }

    // Add mappings for Rules, Topics, Sentences, and Exercises as needed
}