package com.nkhan.configuration.controller.feature.vo.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureAccessNodeRespVO {
    private String featureId;
    private Boolean blocked;
    private Map<String, Boolean> rules;
}
