package com.nkhan.configuration.controller.feature.vo.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureAccessGraphRespVO {
    private List<FeatureAccessNodeRespVO> features;
}
