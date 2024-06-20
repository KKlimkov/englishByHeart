package org.example.englishByHeart.controller;

import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/getLesson/{sentenceId}")
    public Lesson getLesson(@PathVariable Long sentenceId) {
        return lessonService.getLesson(sentenceId);
    }
}

