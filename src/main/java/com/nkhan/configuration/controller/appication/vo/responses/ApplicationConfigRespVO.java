package com.nkhan.configuration.controller.appication.vo.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationConfigRespVO {
    private Long id;
    private String applicationName;
    private String code;
    private String type;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean isActive;
    private String createdBy;
    private String updatedBy;
}
