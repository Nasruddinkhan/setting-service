package com.nkhan.configuration.controller.appication.builder.requests;

import com.nkhan.configuration.common.builder.APIRequestBuilder;
import com.nkhan.configuration.controller.appication.vo.requests.ApplicationConfigCreateReq;
import com.nkhan.configuration.mapper.ApplicationConfigMapper;
import com.nkhan.configuration.model.ApplicationConfigEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationConfigCreateReqBuilder extends APIRequestBuilder<ApplicationConfigCreateReq, ApplicationConfigEntity> {
    private final ApplicationConfigMapper configMapper;
    @Override
    protected ApplicationConfigEntity transformRequest(ApplicationConfigCreateReq applicationConfigCreateReq) {
        return configMapper.toApplicationConfigEntity(applicationConfigCreateReq);
    }
}
