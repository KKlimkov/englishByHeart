package org.example.englishByHeart.service;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.domain.Translation;
import org.example.englishByHeart.domain.TranslationRule;
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


    public List<Rule> getAllRulesByUser(Long userId) {
        List<Rule> rules = ruleRepository.findByUserId(userId);
        return rules;
    }

    public Set<Long> getTranslationIdsForRules(List<Rule> rules) {
        TranslationRule translationRuleLink = new TranslationRule();
        Translation  translation = new Translation();
        return rules.stream()
                .flatMap(rule -> rule.getTranslationRule().stream()
                        .map(TranslationRule::getTranslation)
                        .map(Translation::getTranslateId))
                .collect(Collectors.toSet());
    }

    // New method to update rule
    public Rule updateRule(Long ruleId, String newRule, String newLink) {
        Rule rule = ruleRepository.findById(ruleId).orElse(null);
        if (rule != null) {
            rule.setRule(newRule);
            rule.setLink(newLink);
            return ruleRepository.save(rule);
        }
        return null;
    }

    // New method to delete rule by ruleId
    public boolean deleteRule(Long ruleId) {
        if (ruleRepository.existsById(ruleId)) {
            ruleRepository.deleteById(ruleId);
            return true;
        }
        return false;
    }


}
