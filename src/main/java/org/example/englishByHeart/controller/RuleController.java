package org.example.englishByHeart.controller;

import org.example.englishByHeart.Service.RuleService;
import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.dto.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/rules")
    public Rule createRule(@RequestBody RuleDTO ruleDTO) {
        Rule rule = new Rule();
        rule.setRule(ruleDTO.getRule());
        rule.setLink(ruleDTO.getLink());
        return ruleService.createRule(rule);
    }

    @GetMapping("/rules")
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/getRuleById/{ruleId}")
    public Rule getRuleById(@PathVariable Long ruleId) {
        return ruleService.getRuleById(ruleId);
    }

    @GetMapping("/getRulesByRuleIds")
    public List<Rule> getRulesByRuleIds(@RequestParam List<Long> ruleIds) {
        return ruleService.getRulesByRuleIds(ruleIds);
    }

    @GetMapping("/getTranslationIdsByRuleId/{ruleId}")
    public Set<Long> getTranslationIdsForRule(@PathVariable Long ruleId) {
        Rule rule = ruleService.getRuleById(ruleId);
        return rule != null ? ruleService.getTranslationIdsForRules(List.of(rule)) : null;
    }

    @GetMapping("/getTranslationIdsByRuleIds")
    public Set<Long> getTranslationIdsForRules(@RequestParam List<Long> ruleIds) {
        List<Rule> rules = ruleService.getRulesByRuleIds(ruleIds);
        return ruleService.getTranslationIdsForRules(rules);
    }
}
