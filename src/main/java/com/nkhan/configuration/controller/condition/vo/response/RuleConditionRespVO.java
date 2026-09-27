package com.nkhan.configuration.controller.condition.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleConditionRespVO {
    private Long conditionId;
    private Long ruleId;
    private String attributeKey;
    private Set<String> attributeValue;
}
