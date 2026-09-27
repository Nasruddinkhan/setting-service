package com.nkhan.configuration.mapper;

import com.nkhan.configuration.common.CommonConstants;
import com.nkhan.configuration.model.RuleConditionEntity;
import com.nkhan.configuration.controller.condition.vo.request.RuleConditionReq;
import com.nkhan.configuration.controller.condition.vo.response.RuleConditionRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = CommonConstants.SPRING)
public interface RuleConditionMapper {

    @Mapping(target = "rule", ignore = true)
    @Mapping(target = "attribute", ignore = true)
    RuleConditionEntity toEntity(RuleConditionReq req);

    @Mapping(target = "ruleId", source = "rule.ruleId")
    @Mapping(target = "attributeKey", source = "attribute.attributeKey")
    RuleConditionRespVO toRespVO(RuleConditionEntity entity);

    List<RuleConditionRespVO> toRespVOs(List<RuleConditionEntity> entities);
}
