package org.example.englishByHeart.controller;

import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/getLesson/{sentenceId}")
    public Lesson getLesson(@PathVariable Long sentenceId) {
        return lessonService.getLesson(sentenceId);
    }

    @PostMapping("/startLesson")
    public Map<String, Object> startLesson(@RequestParam Long userId) {
        return lessonService.startLesson(userId);
    }

    @PutMapping("/updateLesson")
    public void updateLesson(@RequestParam Long userId) {
        lessonService.updateLesson(userId);
    }
}

