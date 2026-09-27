package com.nkhan.configuration.model;

import com.nkhan.configuration.common.converter.BooleanToYnConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "FEATURE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeatureEntity  extends  BaseAuditableEntity {

    @Id
    @Column(name = "FEATURE_ID", nullable = false, length = 100)
    private String featureId;

    @Column(name = "DISPLAY_NAME", nullable = false, length = 255)
    private String displayName;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Builder.Default
    @Column(name = "BLOCKED_GLOBALLY", nullable = false)
    @Convert(converter = BooleanToYnConverter.class)
    private Boolean blockedGlobally = Boolean.FALSE;

    @Column(name = "MSG_EN", length = 2000)
    private String msgEn;

    @Column(name = "MSG_AR", length = 2000)
    private String msgAr;


}
