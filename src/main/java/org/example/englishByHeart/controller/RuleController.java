package org.example.englishByHeart.controller;

import org.example.englishByHeart.domain.Topic;
import org.example.englishByHeart.service.RuleService;
import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.dto.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping(value = "/rules", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Rule createRule(@RequestBody RuleDTO ruleDTO) {
        Rule rule = new Rule();
        rule.setRule(ruleDTO.getRule());
        rule.setLink(ruleDTO.getLink());
        rule.setUserId(1L);//hardcore
        //rule.setUserId(ruleDTO.getUserId());
        return ruleService.createRule(rule);
    }

    @GetMapping("/rules")
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/rulesByUserId")
    public List<Rule> getAllRulessByUser(Long userId) {
        return ruleService.getAllRulesByUser(userId);
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

    @PutMapping("/rule/{ruleId}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long ruleId,
                                           @RequestParam String newRule,
                                           @RequestParam(required = false) String newLink) {
        Rule updatedRule = ruleService.updateRule(ruleId, newRule, newLink);
        if (updatedRule != null) {
            return ResponseEntity.ok(updatedRule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // New endpoint to delete rule
    @DeleteMapping("/rule/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        if (ruleService.deleteRule(ruleId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
