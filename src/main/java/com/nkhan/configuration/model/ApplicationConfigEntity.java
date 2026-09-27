package com.nkhan.configuration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "APPLICATION_CONFIG",
        uniqueConstraints = @UniqueConstraint(
                name = "UQ_APP_CONFIG",
                columnNames = {"APPLICATION_NAME", "CODE"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ApplicationConfigEntity extends BaseAuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLICATION_CONFIG_SEQ_GEN")
    @SequenceGenerator(
            name           = "APPLICATION_CONFIG_SEQ_GEN",
            sequenceName   = "APPLICATION_CONFIG_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "APPLICATION_NAME", nullable = false, length = 100)
    private String applicationName;

    @Column(name = "CODE", nullable = false, length = 100)
    private String code;

    @Column(name = "TYPE", nullable = false, length = 50)
    private String type;

}