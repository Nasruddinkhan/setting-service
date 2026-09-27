package com.nkhan.configuration.repository;

import com.nkhan.configuration.model.FeatureRuleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRuleRepo extends JpaRepository<FeatureRuleEntity, Long> {
    Optional<FeatureRuleEntity> findByRuleCode(String ruleCode);

    @EntityGraph(attributePaths = {"feature"})
    List<FeatureRuleEntity> findAll();
}
