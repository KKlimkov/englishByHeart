package org.example.englishByHeart.Service;

import org.example.englishByHeart.domain.Rule;
import org.example.englishByHeart.repos.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public Rule createRule(Rule rule) {
        return ruleRepository.save(rule);
    }
}
