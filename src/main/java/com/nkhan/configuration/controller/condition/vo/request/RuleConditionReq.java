package com.nkhan.configuration.controller.condition.vo.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RuleConditionReq {

    @NotNull
    @Positive
    private Long ruleId;

    @NotBlank
    @Size(max = 100)
    private String attributeKey;


    private Set<String> attributeValue;

}
