package com.nkhan.configuration.mapper;

import com.nkhan.configuration.common.CommonConstants;
import com.nkhan.configuration.model.FeatureRuleEntity;
import com.nkhan.configuration.controller.featurerule.vo.request.FeatureRuleReq;
import com.nkhan.configuration.controller.featurerule.vo.response.FeatureRuleRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = CommonConstants.SPRING)
public interface FeatureRuleMapper {

    @Mapping(target = "createDate", source = "createDate",
            defaultExpression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy",
            defaultValue = "SYSTEM")
    @Mapping(target = "isActive", source = "isActive",
            defaultValue = "true")
    @Mapping(target = "feature", ignore = true)
    FeatureRuleEntity toEntity(FeatureRuleReq req);

    @Mapping(target = "featureId", source = "feature.featureId")
    FeatureRuleRespVO toRespVO(FeatureRuleEntity entity);

    List<FeatureRuleRespVO> toRespVOs(List<FeatureRuleEntity> entities);
}
