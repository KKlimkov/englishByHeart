package org.example.englishByHeart.controller;

import org.example.englishByHeart.Service.RuleService;
import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.dto.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping
    public Rule createRule(@RequestBody RuleDTO ruleDTO) {
        Rule rule = new Rule();
        rule.setRule(ruleDTO.getRule());
        rule.setLink(ruleDTO.getLink());
        return ruleService.createRule(rule);
    }
}
