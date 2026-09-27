package com.nkhan.configuration.service.imp;

import com.nkhan.configuration.repository.UserAttributeRepo;
import com.nkhan.configuration.common.exception.ResourceNotFoundException;
import com.nkhan.configuration.model.RuleConditionEntity;
import com.nkhan.configuration.repository.RuleConditionRepo;
import com.nkhan.configuration.service.RuleConditionService;
import com.nkhan.configuration.repository.FeatureRuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RuleConditionServiceImpl implements RuleConditionService {

    private final RuleConditionRepo ruleConditionRepo;
    private final FeatureRuleRepo featureRuleRepo;
    private final UserAttributeRepo userAttributeRepo;

    @Override
    @Transactional(readOnly = true)
    public List<RuleConditionEntity> findAllRuleConditions() {
        return ruleConditionRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RuleConditionEntity findRuleConditionById(Long conditionId) {
        return getRuleCondition(conditionId);
    }

    @Override
    @Transactional
    public RuleConditionEntity createRuleCondition(RuleConditionEntity req, Long ruleId, String attributeKey) {
        req.setRule(featureRuleRepo.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature rule not found: " + ruleId)));
        req.setAttribute(userAttributeRepo.findById(attributeKey)
                .orElseThrow(() -> new ResourceNotFoundException("User attribute not found: " + attributeKey)));
        return ruleConditionRepo.save(req);
    }

    @Override
    @Transactional
    public RuleConditionEntity updateRuleCondition(Long conditionId, RuleConditionEntity req, Long ruleId, String attributeKey) {
        RuleConditionEntity managed = getRuleCondition(conditionId);
        managed.setRule(featureRuleRepo.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature rule not found: " + ruleId)));
        managed.setAttribute(userAttributeRepo.findById(attributeKey)
                .orElseThrow(() -> new ResourceNotFoundException("User attribute not found: " + attributeKey)));
        managed.setAttributeValue(req.getAttributeValue());
        return ruleConditionRepo.save(managed);
    }

    @Override
    @Transactional
    public RuleConditionEntity addAttributeValues(Long conditionId, Set<String> values) {
        RuleConditionEntity managed = getRuleCondition(conditionId);
        Set<String> mergedValues = new LinkedHashSet<>();
        if (managed.getAttributeValue() != null) {
            mergedValues.addAll(managed.getAttributeValue());
        }
        mergedValues.addAll(values);
        managed.setAttributeValue(mergedValues);
        return ruleConditionRepo.save(managed);
    }

    @Override
    @Transactional
    public void deleteRuleCondition(Long conditionId) {
        ruleConditionRepo.delete(getRuleCondition(conditionId));
    }

    private RuleConditionEntity getRuleCondition(Long conditionId) {
        return ruleConditionRepo.findById(conditionId)
                .orElseThrow(() -> new ResourceNotFoundException("Rule condition not found: " + conditionId));
    }
}
