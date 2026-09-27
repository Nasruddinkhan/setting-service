package com.nkhan.configuration.controller.feature.vo.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedFeatureRespVO {
    private String featureId;
    private String displayName;
    private String description;
    private String blockedBy;
    private Boolean blockedGlobally;
    private Long ruleId;
    private String ruleCode;
    private String msgEn;
    private String msgAr;
    private String denyMsgEn;
    private String denyMsgAr;
}
