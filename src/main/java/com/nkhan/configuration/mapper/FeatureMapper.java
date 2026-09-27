package com.nkhan.configuration.mapper;

import com.nkhan.configuration.common.CommonConstants;
import com.nkhan.configuration.controller.feature.vo.requests.FeatureReq;
import com.nkhan.configuration.controller.feature.vo.responses.FeatureRespVO;
import com.nkhan.configuration.model.FeatureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = CommonConstants.SPRING)
public interface FeatureMapper {
    @Mapping(target = "createDate", source = "createDate",
            defaultExpression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy",
            defaultValue = "SYSTEM")
    @Mapping(target = "isActive", source = "isActive",
            defaultValue = "true")
    @Mapping(target = "blockedGlobally", source = "blockedGlobally",
            defaultValue = "true")
    FeatureEntity toEntity(FeatureReq req);

    FeatureRespVO toRespVO(FeatureEntity entity);

    List<FeatureRespVO> toRespVOs(List<FeatureEntity> entities);
}
