package com.nkhan.configuration.controller.featurerule.vo.response;

import com.nkhan.configuration.common.model.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureRuleRespVO extends BaseVO {
    private Long ruleId;
    private String featureId;
    private Long endpointGroupId;
    private Boolean blocked;
    private LocalDate startDate;
    private LocalDate endDate;
    private String denyMsgEn;
    private String denyMsgAr;
    private String ruleCode;

}
