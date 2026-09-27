package com.nkhan.configuration.service;

import com.nkhan.configuration.model.FeatureRuleEntity;

import java.util.List;
import java.util.Optional;

public interface FeatureRuleService {
    List<FeatureRuleEntity> findAllFeatureRules();

    FeatureRuleEntity findFeatureRuleById(Long ruleId);

    FeatureRuleEntity createFeatureRule(FeatureRuleEntity req, String featureId);

    FeatureRuleEntity updateFeatureRule(Long ruleId, FeatureRuleEntity req, String featureId);

    void deleteFeatureRule(Long ruleId);

}
