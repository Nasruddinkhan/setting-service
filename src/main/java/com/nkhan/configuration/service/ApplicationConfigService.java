package com.nkhan.configuration.service;

import com.nkhan.configuration.model.ApplicationConfigEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface ApplicationConfigService {
    List<ApplicationConfigEntity> findAllConfiguration();

    ApplicationConfigEntity createConfiguration(ApplicationConfigEntity reqBO);

    ApplicationConfigEntity updateConfiguration(Long id, ApplicationConfigEntity req);

    void deleteConfiguration(Long id, String updatedBy);

    void deleteConfigurationPermanently(Long id);

    Optional<ApplicationConfigEntity> getFeatureByCode(String featureId);

    Optional<ApplicationConfigEntity> getFeatureByCode(String code, String type);

    List<ApplicationConfigEntity> findAllConfigurationByType(@NotNull String featureType);
}
