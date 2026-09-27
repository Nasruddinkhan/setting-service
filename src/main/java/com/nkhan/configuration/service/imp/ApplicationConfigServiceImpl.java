package com.nkhan.configuration.service.imp;

import com.nkhan.configuration.common.exception.DuplicateResourceException;
import com.nkhan.configuration.common.exception.ResourceNotFoundException;
import com.nkhan.configuration.common.util.LanguageUtil;
import com.nkhan.configuration.model.ApplicationConfigEntity;
import com.nkhan.configuration.repository.ApplicationConfigRepo;
import com.nkhan.configuration.service.ApplicationConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.nkhan.configuration.common.CommonConstants.DUPLICATE_ERROR_MSG;
import static com.nkhan.configuration.common.CommonConstants.ID_NOT_ALLOWED_MSG;
import static com.nkhan.configuration.common.CommonConstants.SYSTEM;
@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

    private final ApplicationConfigRepo applicationConfigRepo;
    private final LanguageUtil languageUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationConfigEntity> findAllConfiguration() {
        log.debug("ApplicationConfigServiceImpl.findAllConfiguration start");
        final var configEntities = applicationConfigRepo.findAll();
        log.debug("ApplicationConfigServiceImpl.findAllConfiguration end configEntities = {}", configEntities);
        return configEntities;
    }

    @Override
    @Transactional
    public ApplicationConfigEntity createConfiguration(ApplicationConfigEntity req) {
        log.debug("ApplicationConfigServiceImpl.createConfiguration start applicationName = {}, code = {}",
                req.getApplicationName(), req.getCode());

        validateUnique(req.getApplicationName(), req.getCode(), null);
        req.setCreatedBy(defaultedBy(req.getCreatedBy()));
        final var saved = applicationConfigRepo.save(req);

        log.debug("ApplicationConfigServiceImpl.createConfiguration end savedId = {}", saved.getId());
        return saved;
    }

    @Transactional
    @Override
    public ApplicationConfigEntity updateConfiguration(Long id, ApplicationConfigEntity req) {
        log.debug("ApplicationConfigServiceImpl.updateConfiguration start id = {}, applicationName = {}, code = {}",
                id, req.getApplicationName(), req.getCode());

        ApplicationConfigEntity managed = getExistingConfig(id);
        validateUnique(req.getApplicationName(), req.getCode(), id);
        managed.setApplicationName(req.getApplicationName());
        managed.setCode(req.getCode());
        managed.setType(req.getType());
        managed.setUpdatedBy(defaultedBy(req.getUpdatedBy()));
        final var saved = applicationConfigRepo.save(managed);

        log.debug("ApplicationConfigServiceImpl.updateConfiguration end updatedId = {}", saved.getId());
        return saved;
    }

    @Transactional
    @Override
    public void deleteConfiguration(Long id, String updatedBy) {
        log.debug("ApplicationConfigServiceImpl.deleteConfiguration start id = {}", id);

        ApplicationConfigEntity managed = getExistingConfig(id);
        managed.setIsActive(Boolean.FALSE);
        managed.setUpdatedBy(defaultedBy(updatedBy));
        applicationConfigRepo.save(managed);

        log.debug("ApplicationConfigServiceImpl.deleteConfiguration end softDeletedId = {}", id);
    }

    @Override
    @Transactional
    public void deleteConfigurationPermanently(Long id) {
        log.debug("ApplicationConfigServiceImpl.deleteConfigurationPermanently start id = {}", id);
        applicationConfigRepo.delete(getExistingConfig(id));
        log.debug("ApplicationConfigServiceImpl.deleteConfigurationPermanently end deletedId = {}", id);
    }

    @Override
    public Optional<ApplicationConfigEntity> getFeatureByCode(String featureId) {
        return applicationConfigRepo.findByCodeAndIsActiveTrue(featureId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationConfigEntity> getFeatureByCode(String featureId, String type) {
        final var result =  applicationConfigRepo.findByCodeAndTypeAndIsActiveTrue(featureId, type);
        log.info("featureId={}, type={}, found={}",
                featureId, type, result);
        return result;
    }

    @Override
    public List<ApplicationConfigEntity> findAllConfigurationByType(String featureType) {
        return applicationConfigRepo.findByType(featureType);
    }


    private ApplicationConfigEntity getExistingConfig(Long id) {
        return applicationConfigRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        languageUtil.getMessage(ID_NOT_ALLOWED_MSG, id)));
    }
    private String defaultedBy(String updatedBy) {
        return updatedBy != null ? updatedBy : SYSTEM;
    }
    private void validateUnique(String applicationName, String code, Long excludeId) {
        applicationConfigRepo.findByApplicationNameAndCode(applicationName, code)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(excludeId)) {
                        log.debug("ApplicationConfigServiceImpl.validateUnique duplicate found applicationName = {}, code = {}, excludeId = {}",
                                applicationName, code, excludeId);
                        throw new DuplicateResourceException(
                                languageUtil.getMessage(DUPLICATE_ERROR_MSG,
                                        applicationName, code));
                    }
                });
    }
}