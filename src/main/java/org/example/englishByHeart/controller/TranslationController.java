package org.example.englishByHeart.controller;

import org.example.englishByHeart.Service.TranslationService;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.dto.TranslationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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