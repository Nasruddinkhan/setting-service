package com.nkhan.configuration.mapper;

import com.nkhan.configuration.model.UserAttributeEntity;
import com.nkhan.configuration.controller.attribute.vo.request.UserAttributeReq;
import com.nkhan.configuration.controller.attribute.vo.response.UserAttributeRespVO;
import com.nkhan.configuration.common.CommonConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = CommonConstants.SPRING)
public interface UserAttributeMapper {
    @Mapping(target = "createDate", source = "createDate",
            defaultExpression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy",
            defaultValue = "SYSTEM")
    @Mapping(target = "isActive", source = "isActive",
            defaultValue = "true")
    UserAttributeEntity toEntity(UserAttributeReq req);

    UserAttributeRespVO toRespVO(UserAttributeEntity entity);

    List<UserAttributeRespVO> toRespVOs(List<UserAttributeEntity> entities);
}
