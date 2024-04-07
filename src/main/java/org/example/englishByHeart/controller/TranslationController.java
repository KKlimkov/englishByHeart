package org.example.englishByHeart.controller;

import org.example.englishByHeart.Service.TranslationService;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.TranslationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/translations")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping
    public Translation createTranslation(@RequestBody TranslationRequest translationRequest) {
        return translationService.createTranslation(translationRequest);
    }

    @GetMapping("/sentenceIds")
    public Set<Long> getSentenceIdsByTranslationIds(@RequestParam Set<Long> translationIds) {
        return translationService.getSentenceIdsByTranslationIds(translationIds);
    }
}