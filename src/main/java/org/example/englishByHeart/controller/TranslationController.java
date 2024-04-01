package org.example.englishByHeart.controller;

import jakarta.validation.Valid;
import org.example.englishByHeart.Service.TranslationService;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.TranslationRequest;
import org.example.englishByHeart.repos.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/translations")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping
    public Translation createTranslation(@RequestBody TranslationRequest translationRequest) {
        return translationService.createTranslation(translationRequest);
    }
}