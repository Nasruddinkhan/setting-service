package com.nkhan.configuration.mapper;

import com.nkhan.configuration.common.CommonConstants;
import com.nkhan.configuration.controller.appication.vo.requests.ApplicationConfigCreateReq;
import com.nkhan.configuration.controller.appication.vo.responses.ApplicationConfigRespVO;
import com.nkhan.configuration.model.ApplicationConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = CommonConstants.SPRING)
public interface ApplicationConfigMapper {
    List<ApplicationConfigRespVO> toApplicationConfigRespVOs(List<ApplicationConfigEntity> applicationConfigEntities);
    @Mapping(target = "createDate", source = "createDate",
            defaultExpression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy",
            defaultValue = "SYSTEM")
    @Mapping(target = "isActive", source = "isActive",
            defaultValue = "true")
    ApplicationConfigEntity toApplicationConfigEntity(ApplicationConfigCreateReq applicationConfigCreateReq);

    ApplicationConfigRespVO toApplicationConfigRespVO(ApplicationConfigEntity entity);
}
