package com.nkhan.configuration.controller.appication.builder.responses;

import com.nkhan.configuration.common.builder.APIResponseBuilder;
import com.nkhan.configuration.controller.appication.vo.responses.ApplicationConfigRespVO;
import com.nkhan.configuration.mapper.ApplicationConfigMapper;
import com.nkhan.configuration.model.ApplicationConfigEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationConfigRespBuilder extends APIResponseBuilder<List<ApplicationConfigEntity>, List<ApplicationConfigRespVO>> {
    private final ApplicationConfigMapper configMapper;
    @Override
    protected List<ApplicationConfigRespVO> transformMessage(List<ApplicationConfigEntity> serviceResponse) {
        if (serviceResponse == null || serviceResponse.isEmpty()){
            return List.of();
        }
        return configMapper.toApplicationConfigRespVOs(serviceResponse);
    }



}
