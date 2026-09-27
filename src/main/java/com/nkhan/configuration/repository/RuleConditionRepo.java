package com.nkhan.configuration.repository;

import com.nkhan.configuration.model.RuleConditionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleConditionRepo extends JpaRepository<RuleConditionEntity, Long> {
    @EntityGraph(attributePaths = {"rule", "rule.feature", "attribute"})
    List<RuleConditionEntity> findAll();
}
