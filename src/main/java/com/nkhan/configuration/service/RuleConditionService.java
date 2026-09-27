package com.nkhan.configuration.service;

import com.nkhan.configuration.model.RuleConditionEntity;

import java.util.List;
import java.util.Set;

public interface RuleConditionService {
    List<RuleConditionEntity> findAllRuleConditions();

    RuleConditionEntity findRuleConditionById(Long conditionId);

    RuleConditionEntity createRuleCondition(RuleConditionEntity req, Long ruleId, String attributeKey);

    RuleConditionEntity updateRuleCondition(Long conditionId, RuleConditionEntity req, Long ruleId, String attributeKey);

    RuleConditionEntity addAttributeValues(Long conditionId, Set<String> values);

    void deleteRuleCondition(Long conditionId);
}
