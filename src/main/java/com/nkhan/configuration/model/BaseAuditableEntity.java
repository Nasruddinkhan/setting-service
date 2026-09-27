package com.nkhan.configuration.model;

import com.nkhan.configuration.common.converter.BooleanToNumberConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseAuditableEntity {

    @Builder.Default
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;

    @Builder.Default
    @Column(name = "IS_ACTIVE", nullable = false)
    @Convert(converter = BooleanToNumberConverter.class)

    private Boolean isActive = Boolean.TRUE;

    @Column(name = "CREATED_BY", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;



    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createDate = createDate == null ? now : createDate;
        updateDate = updateDate == null ? now : updateDate;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }
}
