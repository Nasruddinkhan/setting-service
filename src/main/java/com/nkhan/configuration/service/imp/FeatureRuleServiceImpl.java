package com.nkhan.configuration.service.imp;

import com.nkhan.configuration.common.exception.DuplicateResourceException;
import com.nkhan.configuration.common.exception.ResourceNotFoundException;
import com.nkhan.configuration.common.util.LanguageUtil;
import com.nkhan.configuration.model.FeatureEntity;
import com.nkhan.configuration.model.FeatureRuleEntity;
import com.nkhan.configuration.repository.FeatureRuleRepo;
import com.nkhan.configuration.service.ApplicationConfigService;
import com.nkhan.configuration.service.FeatureRuleService;
import com.nkhan.configuration.service.FeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nkhan.configuration.common.CommonConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureRuleServiceImpl implements FeatureRuleService {

    private final FeatureRuleRepo featureRuleRepo;
    private final FeatureService featureService;
    private final ApplicationConfigService configService;
    private final LanguageUtil languageUtil;

    @Override
    @Transactional(readOnly = true)
    public List<FeatureRuleEntity> findAllFeatureRules() {
        return featureRuleRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureRuleEntity findFeatureRuleById(Long ruleId) {
        return getFeatureRule(ruleId);
    }

    @Override
    @Transactional
    public FeatureRuleEntity createFeatureRule(
            FeatureRuleEntity request,
            String featureId) {

        FeatureEntity feature = featureService.findFeatureById(featureId);
        validateRuleExists(request.getRuleCode(), featureId);
        validateDuplicateRule(request.getRuleCode(), null);
        request.setFeature(feature);
        request.setBlocked(defaultBoolean(request.getBlocked()));
        log.debug(
                "Creating feature rule. featureId={}, ruleCode={}",
                featureId,
                request.getRuleCode()
        );
        return featureRuleRepo.save(request);
    }

    @Override
    @Transactional
    public FeatureRuleEntity updateFeatureRule(
            Long ruleId,
            FeatureRuleEntity request,
            String featureId) {

        FeatureRuleEntity managed = getFeatureRule(ruleId);
        FeatureEntity feature = featureService.findFeatureById(featureId);
        validateRuleExists(request.getRuleCode(), featureId);
        validateDuplicateRule(request.getRuleCode(), ruleId);
        managed.setFeature(feature);
        managed.setRuleCode(request.getRuleCode());
        managed.setBlocked(defaultBoolean(request.getBlocked()));
        managed.setStartDate(request.getStartDate());
        managed.setEndDate(request.getEndDate());
        managed.setDenyMsgEn(request.getDenyMsgEn());
        managed.setDenyMsgAr(request.getDenyMsgAr());

        log.debug(
                "Updating feature rule. ruleId={}, featureId={}, ruleCode={}",
                ruleId,
                featureId,
                request.getRuleCode()
        );

        return featureRuleRepo.save(managed);
    }

    @Override
    @Transactional
    public void deleteFeatureRule(Long ruleId) {
        FeatureRuleEntity rule = getFeatureRule(ruleId);
        featureRuleRepo.delete(rule);
    }

    private FeatureRuleEntity getFeatureRule(Long ruleId) {
        return featureRuleRepo.findById(ruleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Feature rule not found: " + ruleId
                        )
                );
    }

    private void validateRuleExists(String ruleCode, String featureId) {

        boolean exists = configService
                .getFeatureByCode(ruleCode, featureId)
                .isPresent();

        if (!exists) {
            throw new ResourceNotFoundException(
                    languageUtil.getMessage(
                            FEATURE_RULE_MUST_BE_PRESENT,
                            ruleCode,
                            featureId
                    )
            );
        }
    }

    private void validateDuplicateRule(String ruleCode, Long currentRuleId) {

        featureRuleRepo.findByRuleCode(ruleCode)
                .filter(existing ->
                        !existing.getRuleId().equals(currentRuleId)
                )
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            languageUtil.getMessage(
                                    DUPLICATE_CONFLICT,
                                    ruleCode
                            )
                    );
                });
    }

    private boolean defaultBoolean(Boolean value) {
        return value != null ? value : true;
    }
}