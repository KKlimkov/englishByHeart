package org.example.englishByHeart.controller;

import org.example.englishByHeart.domain.Exercise;
import org.example.englishByHeart.domain.Sentence;
import org.example.englishByHeart.dto.CreateExerciseRequest;
import org.example.englishByHeart.dto.ExercisesStartDTO;
import org.example.englishByHeart.dto.TranslationWithRuleDTO;
import org.example.englishByHeart.repos.ExerciseRepository;
import org.example.englishByHeart.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/start")
    public ResponseEntity<ExercisesStartDTO> getExerciseIdsByTopicsAndRules(@RequestParam Long userId, @RequestParam List<Long> topicIds, @RequestParam List<Long> ruleIds) {

        ResponseEntity<List<Long>> responseIds = exerciseService.getSentencesIdsByTopicsAndRules(topicIds, ruleIds);

        if (responseIds.getStatusCode() != HttpStatus.OK) {
            // If the request to get sentence IDs was not successful, return an error response
            return ResponseEntity.status(responseIds.getStatusCode()).body(null);
        }

        CreateExerciseRequest createExerciseRequest = new CreateExerciseRequest();
        createExerciseRequest.setUserId(userId);
        createExerciseRequest.setCurrentSentencesId(responseIds.getBody());

        Exercise createdExercise = exerciseService.createExercise(createExerciseRequest);

        ExerciseService.PickedElementResponse pickedElementResponse = exerciseService.removeRandomElement(responseIds.getBody());

        List<Exercise> updatedExercises = exerciseService.updateExercisesByUserId(userId, pickedElementResponse.getModifiedArray());

        ResponseEntity<Sentence> sentence = exerciseService.getSentenceById(pickedElementResponse.getPickedElement());

        ResponseEntity<List<TranslationWithRuleDTO>> translationWithRuleDTOResponse = exerciseService.getTranslationsById(pickedElementResponse.getPickedElement());

        ExercisesStartDTO exercisesStartDTO = new ExercisesStartDTO();

        exercisesStartDTO.setLearningSentence(sentence.getBody().getLearningSentence());
        exercisesStartDTO.setComment(sentence.getBody().getComment());
        exercisesStartDTO.setTranslations(translationWithRuleDTOResponse.getBody());

        return ResponseEntity.ok(exercisesStartDTO);
    }
}
