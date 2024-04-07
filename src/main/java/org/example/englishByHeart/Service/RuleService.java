package org.example.englishByHeart.Service;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.domain.TranslationRuleLink;
import org.example.englishByHeart.repos.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public Rule createRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule getRuleById(Long ruleId) {
        return ruleRepository.findById(ruleId).orElse(null);
    }

    public List<Rule> getRulesByRuleIds(List<Long> ruleIds) {
        return ruleRepository.findAllById(ruleIds);
    }


    public Set<Long> getTranslationIdsForRules(List<Rule> rules) {
        TranslationRuleLink translationRuleLink = new TranslationRuleLink();
        Translation  translation = new Translation();
        return rules.stream()
                .flatMap(rule -> rule.getTranslationRuleLinks().stream()
                        .map(TranslationRuleLink::getTranslation)
                        .map(Translation::getTranslateId))
                .collect(Collectors.toSet());
    }
}
