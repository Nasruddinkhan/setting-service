package com.nkhan.configuration.controller.feature.vo.responses;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureRespVO {
    private String featureId;
    private String displayName;
    private String description;
    private Boolean blockedGlobally;
    private String msgEn;
    private String msgAr;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
