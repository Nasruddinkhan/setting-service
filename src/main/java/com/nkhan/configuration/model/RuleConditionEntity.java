package com.nkhan.configuration.model;

import com.nkhan.configuration.common.converter.StringSetToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "RULE_CONDITION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleConditionEntity extends BaseAuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RULE_CONDITION_SEQ_GEN")
    @SequenceGenerator(name = "RULE_CONDITION_SEQ_GEN",allocationSize = 1,
            sequenceName = "RULE_CONDITION_SEQ")
    @Column(name = "CONDITION_ID", nullable = false)
    private Long conditionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RULE_ID", nullable = false)
    private FeatureRuleEntity rule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ATTRIBUTE_KEY", nullable = false)
    private UserAttributeEntity attribute;


    @Lob
    @Column(name = "ATTRIBUTE_VALUE")
    @Convert(converter = StringSetToJsonConverter.class)
    private Set<String> attributeValue;
}
