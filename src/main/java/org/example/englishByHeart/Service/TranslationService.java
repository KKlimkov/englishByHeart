package org.example.englishByHeart.Service;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.domain.TranslationRuleLink;
import org.example.englishByHeart.dto.TranslationRequest;
import org.example.englishByHeart.repos.RuleRepository;
import org.example.englishByHeart.repos.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private RuleRepository ruleRepository;

    public Translation createTranslation(TranslationRequest translationRequest) {
        Translation translation = new Translation();
        translation.setTranslation(translationRequest.getTranslation());
        translation.setSentenceId(translationRequest.getSentenceId());

        List<TranslationRuleLink> translationRuleLinks = new ArrayList<>();
        for (Long ruleId : translationRequest.getRuleIds()) {
            Rule rule = ruleRepository.findById(ruleId)
                    .orElseThrow(() -> new RuntimeException("Rule not found with id: " + ruleId));
            TranslationRuleLink translationRuleLink = new TranslationRuleLink();
            translationRuleLink.setTranslation(translation);
            translationRuleLink.setRule(rule);
            translationRuleLinks.add(translationRuleLink);
        }

        translation.setTranslationRuleLinks(translationRuleLinks);

        return translationRepository.save(translation);
    }

    public Set<Long> getSentenceIdsByTranslationIds(Set<Long> translationIds) {
        return translationRepository.findByTranslateIdIn(translationIds)
                .stream()
                .map(Translation::getSentenceId)
                .collect(Collectors.toSet());
    }
}
