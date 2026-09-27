package com.nkhan.configuration.repository;

import com.nkhan.configuration.model.ApplicationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationConfigRepo extends JpaRepository<ApplicationConfigEntity, Long> {
    Optional<ApplicationConfigEntity> findByApplicationNameAndCode(String applicationName, String code);
    Optional<ApplicationConfigEntity> findByCodeAndIsActiveTrue(String code);

    List<ApplicationConfigEntity> findByType(String type);

    Optional<ApplicationConfigEntity> findByCodeAndTypeAndIsActiveTrue(String type, String featureId);
}
