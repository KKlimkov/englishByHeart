package org.example.englishByHeart.controller;

import jakarta.validation.Valid;
import org.example.englishByHeart.domain.Exercise;
import org.example.englishByHeart.dto.*;
import org.example.englishByHeart.enums.SortBy;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.example.englishByHeart.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    ExerciseRepository exerciseRepository;


    @PostMapping("/removeRandomElement")
    public ExerciseService.PickedElementResponse removeRandomElement(@RequestBody List<Long> array) {
        return exerciseService.removeRandomElement(array);
    }

    @PostMapping("/create")
    public ResponseEntity<Exercise> createExercise(@RequestBody CreateExerciseRequest request) {
        Exercise savedExercise = exerciseService.createExercise(request);
        return new ResponseEntity<>(savedExercise, HttpStatus.CREATED);
    }

    @GetMapping("/currentSentencesIds")
    public ResponseEntity<List<Long>> getCurrentSentencesIdsByUserId(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long exerciseId
    ) {
        List<Long> sentencesIds = exerciseService.getCurrentSentencesIdsByUserId(userId, exerciseId);
        return ResponseEntity.ok(sentencesIds);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> savedExercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(savedExercises);
    }

    @PutMapping("/update")
    public ResponseEntity<List<Exercise>> updateExercisesByUserId(@RequestBody CreateExerciseRequest request) {

        List<Exercise> updatedExercises = exerciseService.updateExercisesByUserId(request.getUserId(), request.getSentencesId());
        if (updatedExercises.isEmpty()) {
            // No exercises found for the given user ID, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
        // Return the updated exercises in the response body with 200 OK status
        return ResponseEntity.ok(updatedExercises);
    }

    @PostMapping("/createExercise")
    @Validated
    public ResponseEntity<Object> createExercise(@Valid @RequestBody CreateExercisePayload payload) {

        if (payload.getUserId() == null || payload.getUserId() <= 0) {
            return ResponseEntity.badRequest().body("userId is missing or invalid");
        }
        if (payload.getExerciseName() == null || payload.getExerciseName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("exerciseName is missing");
        }

        // Extracting data from payload
        Long userId = payload.getUserId();
        String exerciseName = payload.getExerciseName();
        List<Long> topicIds = payload.getTopicIds();
        List<Long> ruleIds = payload.getRuleIds();

        // Service call to get sentence IDs
        ResponseEntity<List<Long>> responseIds = exerciseService.getSentencesIdsByTopicsAndRules(topicIds, ruleIds);

        if (responseIds.getStatusCode() != HttpStatus.OK) {
            // If the request to get sentence IDs was not successful, return an error response
            return ResponseEntity.status(responseIds.getStatusCode()).body(null);
        }

        // Creating exercise request
        CreateExerciseRequest createExerciseRequest = new CreateExerciseRequest();
        createExerciseRequest.setUserId(userId);
        createExerciseRequest.setExerciseName(exerciseName);
        createExerciseRequest.setTopicsIds(topicIds);
        createExerciseRequest.setRulesIds(ruleIds);
        createExerciseRequest.setSentencesId(responseIds.getBody());

        // Creating the exercise
        Exercise createdExercise = exerciseService.createExercise(createExerciseRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/exercisesByUserIdAndSentenceId")
    public ResponseEntity<List<ExerciseResponse>> getExercisesByUserId(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long exerciseId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) SortBy sortBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction mode
    ) {
        List<ExerciseResponse> exercises = exerciseService.getExercisesByUserId(userId, exerciseId, isActive, sortBy, mode);
        return ResponseEntity.ok(exercises);
    }


    @PutMapping("/updateExercises")
    public ResponseEntity<List<Exercise>> updateExercises(@RequestParam Long userId, @RequestParam String mode) {
        List<Exercise> updatedExercises = exerciseService.updateExercises(userId, mode);
        if (updatedExercises.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedExercises);
    }

    @PutMapping("/activate/{exerciseId}")
    public ResponseEntity<String> activateExercise(@PathVariable Long exerciseId, @RequestParam Long userId) {
        boolean isActivated = exerciseService.activateExercise(exerciseId, userId);
        if (isActivated) {
            return ResponseEntity.ok("Exercise activated successfully.");
        } else {
            return ResponseEntity.status(404).body("Exercise not found.");
        }
    }

    @PutMapping("/restart")
    public String restartExercise(@RequestParam Long userId) {
        exerciseService.restartExercise(userId);
        return "Ok";
    }

    @PutMapping("/updateExerciseByExerciseId/{exerciseId}")
    public ResponseEntity<Exercise> updateExerciseByExerciseId(
            @PathVariable Long exerciseId,
            @RequestBody UpdateExerciseRequest request) {
        try {
            Optional<Exercise> updatedExercise = exerciseService.updateExerciseByExerciseId(exerciseId, request);
            if (updatedExercise.isPresent()) {
                return ResponseEntity.ok(updatedExercise.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/exercises/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long exerciseId) {
        boolean isDeleted = exerciseService.deleteExerciseById(exerciseId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
