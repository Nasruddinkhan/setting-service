package com.nkhan.configuration.model;

import com.nkhan.configuration.common.converter.BooleanToYnConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "FEATURE_RULE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_FEATURE_RULE",
                        columnNames = {"FEATURE_ID", "RULE_CODE"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeatureRuleEntity extends BaseAuditableEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FEATURE_RULE_SEQ_GEN"
    )
    @SequenceGenerator(
            name = "FEATURE_RULE_SEQ_GEN",
            sequenceName = "FEATURE_RULE_SEQ",
            allocationSize = 1
    )
    @Column(name = "RULE_ID", nullable = false)
    private Long ruleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FEATURE_ID", nullable = false)
    private FeatureEntity feature;

    @Column(name = "RULE_CODE", nullable = false, length = 100)
    private String ruleCode;

    @Convert(converter = BooleanToYnConverter.class)
    @Column(name = "BLOCKED", nullable = false, length = 1)
    @Builder.Default
    private Boolean blocked = Boolean.TRUE;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DENY_MSG_EN", length = 500)
    private String denyMsgEn;

    @Column(name = "DENY_MSG_AR", length = 500)
    private String denyMsgAr;
}
