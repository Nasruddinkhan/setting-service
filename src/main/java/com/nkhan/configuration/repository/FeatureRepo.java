package com.nkhan.configuration.repository;

import com.nkhan.configuration.model.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepo extends JpaRepository<FeatureEntity, String> {
    List<FeatureEntity> findByBlockedGloballyTrue();
}
