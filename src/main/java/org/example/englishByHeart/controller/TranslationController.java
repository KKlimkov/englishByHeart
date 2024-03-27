package org.example.englishByHeart.controller;

import jakarta.validation.Valid;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.repos.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    @Autowired
    private TranslationRepository translationRepository;

    @GetMapping
    public List<Translation> getAllTranslations() {
        return translationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<CustomArrayResponse<Integer>> createTranslation(@Valid @RequestBody List<Translation> translations, BindingResult result) {
        CustomArrayResponse<Integer> response = new CustomArrayResponse<>();

        // Check if there are validation errors
        if (result.hasErrors()) {
            response.setSuccess(false);
            response.setErrorMessage("Validation failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        List<Integer> translationIds = new ArrayList<>();

        try {
            // Save each translation and collect their ids
            for (Translation translation : translations) {
                // Ensure translationId is null to allow database to generate it
                translation.setTranslationId(null);
                Translation savedTranslation = translationRepository.save(translation);
                translationIds.add(savedTranslation.getTranslationId());
            }

            // Populate the response
            response.setSuccess(true);
            response.setErrorMessage("");
            response.setData(translationIds);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response.setSuccess(false);
            response.setErrorMessage("Failed to create translations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}