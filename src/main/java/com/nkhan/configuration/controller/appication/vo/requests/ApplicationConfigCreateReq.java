package com.nkhan.configuration.controller.appication.vo.requests;

import com.nkhan.configuration.common.model.BaseVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.nkhan.configuration.common.CommonConstants.APP_CODE;
import static com.nkhan.configuration.common.CommonConstants.APP_CODE_SIZE;
import static com.nkhan.configuration.common.CommonConstants.APP_NAME;
import static com.nkhan.configuration.common.CommonConstants.APP_NAME_SIZE;
import static com.nkhan.configuration.common.CommonConstants.APP_TYPE;
import static com.nkhan.configuration.common.CommonConstants.APP_TYPE_SIZE;
import static com.nkhan.configuration.common.CommonConstants.SIZE_100;
import static com.nkhan.configuration.common.CommonConstants.SIZE_50;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ApplicationConfigCreateReq extends BaseVO {

    @NotBlank(message = APP_NAME)
    @Size(message =  APP_NAME_SIZE, max = SIZE_100)
    @JsonProperty("applicationName")
    private String applicationName;

    @JsonProperty("code")
    @NotBlank(message = APP_CODE)
    @Size(max = SIZE_100,  message = APP_CODE_SIZE)
    private String code;

    @JsonProperty("type")
    @NotBlank(message = APP_TYPE)
    @Size(message = APP_TYPE_SIZE, max = SIZE_50)
    private String type;

}