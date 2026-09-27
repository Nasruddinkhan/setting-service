package com.nkhan.configuration.model;

import com.nkhan.configuration.common.enums.AttributeSourceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "USER_ATTRIBUTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserAttributeEntity extends BaseAuditableEntity{

    @Id
    @Column(name = "ATTRIBUTE_KEY", nullable = false, length = 100)
    private String attributeKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE", length = 30)
    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private AttributeSourceType sourceType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "PRIORITY")
    private Integer priority;

}