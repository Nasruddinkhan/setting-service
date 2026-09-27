package com.nkhan.configuration.controller.attribute.vo.request;

import com.nkhan.configuration.common.enums.AttributeSourceType;
import com.nkhan.configuration.common.model.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserAttributeReq extends BaseVO {

    @NotBlank
    @Size(max = 100)
    private String attributeKey;
    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private AttributeSourceType sourceType;

    private Integer priority;


}
