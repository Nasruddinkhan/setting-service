package com.nkhan.configuration.controller.appication.builder.responses;

import com.nkhan.configuration.common.builder.APIResponseBuilder;
import com.nkhan.configuration.controller.appication.vo.responses.ApplicationConfigRespVO;
import com.nkhan.configuration.mapper.ApplicationConfigMapper;
import com.nkhan.configuration.model.ApplicationConfigEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationConfigCreateResBuilder extends APIResponseBuilder<ApplicationConfigEntity, ApplicationConfigRespVO> {

    private final ApplicationConfigMapper configMapper;

    @Override
    protected ApplicationConfigRespVO transformMessage(ApplicationConfigEntity entity) {
        return configMapper.toApplicationConfigRespVO(entity);
    }
}
