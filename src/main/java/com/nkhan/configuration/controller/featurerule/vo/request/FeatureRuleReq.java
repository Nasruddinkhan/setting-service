package com.nkhan.configuration.controller.featurerule.vo.request;

import com.nkhan.configuration.common.model.BaseVO;
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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeatureRuleReq extends BaseVO {

    @NotBlank(message = "Feature ID cannot be empty")
    @Size(max = 100, message = "Feature ID must not exceed 100 characters")
    private String featureId;

    private Boolean blocked;

    private LocalDate startDate;

    private LocalDate endDate;
    @NotBlank(message = "Rule code cannot be empty")
    @Size(max = 100, message = "Rule code must not exceed 100 characters")
    private String ruleCode;

    @NotBlank(message = "English deny message cannot be empty")
    @Size(max = 500, message = "English deny message must not exceed 500 characters")
    private String denyMsgEn;

    @NotBlank(message = "Arabic deny message cannot be empty")
    @Size(max = 500, message = "Arabic deny message must not exceed 500 characters")
    private String denyMsgAr;

    @AssertTrue(message = "endDate must be greater than or equal to startDate")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
