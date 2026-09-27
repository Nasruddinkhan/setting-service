package com.nkhan.configuration.controller.feature.vo.requests;

import com.nkhan.configuration.common.model.BaseVO;
import jakarta.validation.constraints.NotBlank;
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
public class FeatureReq  extends BaseVO {

    @NotBlank
    @Size(max = 100)
    private String featureId;

    @NotBlank
    @Size(max = 255)
    private String displayName;

    @Size(max = 2000)
    private String description;

    private Boolean blockedGlobally;

    @Size(max = 2000)
    private String msgEn;

    @Size(max = 2000)
    private String msgAr;
}
