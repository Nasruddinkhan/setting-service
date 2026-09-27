package com.nkhan.configuration.controller.attribute.vo.response;

import com.nkhan.configuration.common.enums.AttributeSourceType;
import com.nkhan.configuration.common.model.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserAttributeRespVO extends BaseVO {
    private String attributeKey;
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private AttributeSourceType sourceType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer priority;
}
