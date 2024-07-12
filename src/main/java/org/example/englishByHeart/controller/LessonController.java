package org.example.englishByHeart.controller;

import org.example.englishByHeart.dto.Lesson;
import org.example.englishByHeart.service.AllLessonsDoneException;
import org.example.englishByHeart.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> startLesson(@RequestParam Long userId) {
        try {
            Map<String, Object> response = lessonService.startLesson(userId);
            if ("error".equals(response.get("status"))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PutMapping("/updateLesson")
    public ResponseEntity<String> updateLesson(@RequestParam Long userId) {
        try {
            lessonService.updateLesson(userId);
            return ResponseEntity.ok("Lesson updated successfully.");
        } catch (AllLessonsDoneException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}

