package org.example.englishByHeart.controller;

import jakarta.validation.Valid;
import org.example.englishByHeart.domain.Exercise;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.dto.CreateExercisePayload;
import org.example.englishByHeart.dto.CreateExerciseRequest;
import org.example.englishByHeart.dto.ExercisesStartDTO;
import org.example.englishByHeart.dto.TranslationWithRuleDTO;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.example.englishByHeart.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*
    @PostMapping("/create")
    public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {
        Exercise savedExercise = exerciseService.saveExercise(exercise);
        return new ResponseEntity<>(savedExercise, HttpStatus.CREATED);
    }*/

    @PostMapping("/create")
    public ResponseEntity<Exercise> createExercise(@RequestBody CreateExerciseRequest request) {
        Exercise savedExercise = exerciseService.createExercise(request);
        return new ResponseEntity<>(savedExercise, HttpStatus.CREATED);
    }

    @GetMapping("/currentSentencesIds")
    public ResponseEntity<List<Long>> getCurrentSentencesIdsByUserId(@RequestParam Long userId) {
        List<Long> currentSentencesIds = exerciseService.getCurrentSentencesIdsByUserId(userId);
        return ResponseEntity.ok(currentSentencesIds);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> savedExercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(savedExercises);
    }

    @PutMapping("/update")
    public ResponseEntity<List<Exercise>> updateExercisesByUserId(@RequestBody CreateExerciseRequest request) {

        List<Exercise> updatedExercises = exerciseService.updateExercisesByUserId(request.getUserId(), request.getCurrentSentencesId());
        if (updatedExercises.isEmpty()) {
            // No exercises found for the given user ID, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
        // Return the updated exercises in the response body with 200 OK status
        return ResponseEntity.ok(updatedExercises);
    }

    @PostMapping("/createExercise")
    @Validated
    public ResponseEntity<Object> getExerciseIdsByTopicsAndRules(@Valid @RequestBody CreateExercisePayload payload) {

        if (payload.getUserId() == null || payload.getUserId() <= 0) {
            return ResponseEntity.badRequest().body("userId is missing or invalid");
        }
        if (payload.getSentenceName() == null || payload.getSentenceName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("sentenceName is missing");
        }

        // Extracting data from payload
        Long userId = payload.getUserId();
        String sentenceName = payload.getSentenceName();
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
        createExerciseRequest.setSentenceName(sentenceName);
        createExerciseRequest.setCurrentTopicsIds(topicIds);
        createExerciseRequest.setCurrentRulesIds(ruleIds);
        createExerciseRequest.setCurrentSentencesId(responseIds.getBody());

        // Creating the exercise
        Exercise createdExercise = exerciseService.createExercise(createExerciseRequest);
        return ResponseEntity.ok().build();
    }
}
