package com.nkhan.configuration.service.imp;

import com.nkhan.configuration.common.exception.DuplicateResourceException;
import com.nkhan.configuration.common.exception.ResourceNotFoundException;
import com.nkhan.configuration.common.util.LanguageUtil;
import com.nkhan.configuration.controller.feature.vo.responses.BlockedFeatureRespVO;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureAccessGraphRespVO;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureAccessNodeRespVO;
import com.nkhan.configuration.model.FeatureEntity;
import com.nkhan.configuration.model.FeatureRuleEntity;
import com.nkhan.configuration.model.RuleConditionEntity;
import com.nkhan.configuration.repository.FeatureRepo;
import com.nkhan.configuration.repository.FeatureRuleRepo;
import com.nkhan.configuration.repository.RuleConditionRepo;
import com.nkhan.configuration.service.ApplicationConfigService;
import com.nkhan.configuration.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nkhan.configuration.common.CommonConstants.*;


@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepo featureRepo;
    private final FeatureRuleRepo featureRuleRepo;
    private final RuleConditionRepo ruleConditionRepo;
    private final ApplicationConfigService configService;
    private final LanguageUtil languageUtil;

    @Override
    @Transactional(readOnly = true)
    public List<FeatureEntity> findAllFeatures() {
        return featureRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureEntity findFeatureById(String featureId) {
        return getFeature(featureId);
    }

    @Override
    @Transactional
    public FeatureEntity createFeature(FeatureEntity req) {
        if (configService.getFeatureByCode(req.getFeatureId()).isEmpty()){
            throw new ResourceNotFoundException(languageUtil.getMessage(FEATURE_MUST_BE_PRESENT, req.getFeatureId()));
        }
        if (featureRepo.existsById(req.getFeatureId())) {
            throw new DuplicateResourceException(languageUtil.getMessage(DUPLICATE_CONFLICT, req.getFeatureId()));
        }
        req.setBlockedGlobally(req.getBlockedGlobally());
        return featureRepo.save(req);
    }

    @Override
    @Transactional
    public FeatureEntity updateFeature(String featureId, FeatureEntity req) {
        FeatureEntity managed = getFeature(featureId);
        managed.setDisplayName(req.getDisplayName());
        managed.setDescription(req.getDescription());
        managed.setBlockedGlobally(req.getBlockedGlobally());
        managed.setMsgEn(req.getMsgEn());
        managed.setMsgAr(req.getMsgAr());
        return featureRepo.save(managed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockedFeatureRespVO> findGloballyBlockedFeatures() {
        return featureRepo.findByBlockedGloballyTrue().stream()
                .filter(this::isActive)
                .map(this::toGlobalBlockedFeature)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockedFeatureRespVO> findBlockedFeaturesForUsername(String username) {
        Map<String, BlockedFeatureRespVO> blockedFeatures = new LinkedHashMap<>();
        featureRepo.findByBlockedGloballyTrue().stream()
                .filter(this::isActive)
                .map(this::toGlobalBlockedFeature)
                .toList()
                .forEach(feature -> blockedFeatures.put(feature.getFeatureId(), feature));
        ruleConditionRepo.findAll().stream()
                .filter(condition -> conditionMatchesValue(condition, username))
                .filter(condition -> isBlockingRule(condition.getRule()))
                .map(condition -> toRuleBlockedFeature(condition.getRule()))
                .forEach(blockedFeature -> blockedFeatures.putIfAbsent(blockedFeature.getFeatureId(), blockedFeature));
        return List.copyOf(blockedFeatures.values());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Boolean> findFeatureAccessByUsername(String username) {
        Map<String, Boolean> featureAccess = new LinkedHashMap<>();
        Set<Long> blockedRuleIds = getBlockedRuleIds(username);

        featureRepo.findAll().stream()
                .filter(this::isActive)
                .forEach(feature -> featureAccess.put(
                        feature.getFeatureId(),
                        !Boolean.TRUE.equals(feature.getBlockedGlobally())
                ));

        featureRuleRepo.findAll().stream()
                .filter(this::isActive)
                .filter(rule -> rule.getFeature() != null && isActive(rule.getFeature()))
                .filter(rule -> rule.getRuleCode() != null && !rule.getRuleCode().isBlank())
                .forEach(rule -> featureAccess.put(
                        rule.getRuleCode(),
                        isRuleAccessible(rule, blockedRuleIds)
                ));

        return featureAccess;
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureAccessGraphRespVO findFeatureAccessGraphByUsername(String username) {
        Set<Long> blockedRuleIds = getBlockedRuleIds(username);
        Map<String, List<FeatureRuleEntity>> rulesByFeatureId = featureRuleRepo.findAll().stream()
                .filter(this::isActive)
                .filter(rule -> rule.getFeature() != null && isActive(rule.getFeature()))
                .filter(rule -> rule.getRuleCode() != null && !rule.getRuleCode().isBlank())
                .collect(Collectors.groupingBy(
                        rule -> rule.getFeature().getFeatureId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<FeatureAccessNodeRespVO> features = featureRepo.findAll().stream()
                .filter(this::isActive)
                .map(feature -> toFeatureAccessNode(feature, rulesByFeatureId.get(feature.getFeatureId()), blockedRuleIds))
                .toList();

        return FeatureAccessGraphRespVO.builder()
                .features(features)
                .build();
    }

    @Override
    @Transactional
    public void deleteFeature(String featureId) {
        featureRepo.delete(getFeature(featureId));
    }

    private FeatureEntity getFeature(String featureId) {
        return featureRepo.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        languageUtil.getMessage(FEATURE_NOT_ALLOWED_MSG, featureId)));
    }

    private boolean conditionMatchesValue(RuleConditionEntity condition, String value) {
        Set<String> values = condition.getAttributeValue();
        return values != null && values.contains(value);
    }

    private Set<Long> getBlockedRuleIds(String username) {
        return ruleConditionRepo.findAll().stream()
                .filter(condition -> conditionMatchesValue(condition, username))
                .map(RuleConditionEntity::getRule)
                .filter(this::isBlockingRule)
                .map(FeatureRuleEntity::getRuleId)
                .collect(java.util.stream.Collectors.toSet());
    }

    private boolean isRuleAccessible(FeatureRuleEntity rule, Set<Long> blockedRuleIds) {
        if (Boolean.TRUE.equals(rule.getFeature().getBlockedGlobally())) {
            return false;
        }
        if (isEffective(rule)) {
            return true;
        }
        return !blockedRuleIds.contains(rule.getRuleId());
    }

    private boolean isRuleAllowedForUser(FeatureRuleEntity rule, Set<Long> blockedRuleIds) {
        if (isEffective(rule)) {
            return true;
        }
        return !blockedRuleIds.contains(rule.getRuleId());
    }

    private boolean isBlockingRule(FeatureRuleEntity rule) {
        if (rule == null || !Boolean.TRUE.equals(rule.getBlocked()) || !isActive(rule) || isEffective(rule)) {
            return false;
        }
        return rule.getFeature() != null && isActive(rule.getFeature()) && !Boolean.TRUE.equals(rule.getFeature().getBlockedGlobally());
    }

    private boolean isEffective(FeatureRuleEntity rule) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Riyadh"));
        return (rule.getStartDate() != null && today.isBefore(rule.getStartDate()))
                || (rule.getEndDate() != null && today.isAfter(rule.getEndDate()));
    }

    private boolean isActive(com.nkhan.configuration.model.BaseAuditableEntity entity) {
        return entity != null && !Boolean.FALSE.equals(entity.getIsActive());
    }

    private BlockedFeatureRespVO toGlobalBlockedFeature(FeatureEntity feature) {
        return BlockedFeatureRespVO.builder()
                .featureId(feature.getFeatureId())
                .displayName(feature.getDisplayName())
                .description(feature.getDescription())
                .blockedBy("GLOBAL")
                .blockedGlobally(Boolean.TRUE)
                .msgEn(feature.getMsgEn())
                .msgAr(feature.getMsgAr())
                .build();
    }

    private BlockedFeatureRespVO toRuleBlockedFeature(FeatureRuleEntity rule) {
        FeatureEntity feature = rule.getFeature();
        return BlockedFeatureRespVO.builder()
                .featureId(feature.getFeatureId())
                .displayName(feature.getDisplayName())
                .description(feature.getDescription())
                .blockedBy("RULE")
                .blockedGlobally(Boolean.FALSE)
                .ruleId(rule.getRuleId())
                .ruleCode(rule.getRuleCode())
                .msgEn(feature.getMsgEn())
                .msgAr(feature.getMsgAr())
                .denyMsgEn(rule.getDenyMsgEn())
                .denyMsgAr(rule.getDenyMsgAr())
                .build();
    }

    private FeatureAccessNodeRespVO toFeatureAccessNode(
            FeatureEntity feature,
            List<FeatureRuleEntity> rules,
            Set<Long> blockedRuleIds) {
        Map<String, Boolean> ruleAccess = new LinkedHashMap<>();
        if (rules != null) {
            rules.forEach(rule -> ruleAccess.put(rule.getRuleCode(), isRuleAllowedForUser(rule, blockedRuleIds)));
        }

        return FeatureAccessNodeRespVO.builder()
                .featureId(feature.getFeatureId())
                .blocked(Boolean.TRUE.equals(feature.getBlockedGlobally()))
                .rules(ruleAccess)
                .build();
    }

}
