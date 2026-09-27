package com.nkhan.configuration.service.imp;

import com.nkhan.configuration.model.UserAttributeEntity;
import com.nkhan.configuration.repository.UserAttributeRepo;
import com.nkhan.configuration.service.UserAttributeService;
import com.nkhan.configuration.common.exception.DuplicateResourceException;
import com.nkhan.configuration.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAttributeServiceImpl implements UserAttributeService {

    private final UserAttributeRepo userAttributeRepo;

    @Override
    @Transactional(readOnly = true)
    public List<UserAttributeEntity> findAllUserAttributes() {
        return userAttributeRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAttributeEntity findUserAttributeById(String attributeKey) {
        return getUserAttribute(attributeKey);
    }

    @Override
    @Transactional
    public UserAttributeEntity createUserAttribute(UserAttributeEntity req) {
        if (userAttributeRepo.existsById(req.getAttributeKey())) {
            throw new DuplicateResourceException("User attribute already exists: " + req.getAttributeKey());
        }
        return userAttributeRepo.save(req);
    }

    @Override
    @Transactional
    public UserAttributeEntity updateUserAttribute(String attributeKey, UserAttributeEntity req) {
        UserAttributeEntity managed = getUserAttribute(attributeKey);
        managed.setSourceType(req.getSourceType());
        managed.setStartDate(req.getStartDate());
        managed.setEndDate(req.getEndDate());
        managed.setPriority(req.getPriority());
        return userAttributeRepo.save(managed);
    }

    @Override
    @Transactional
    public void deleteUserAttribute(String attributeKey) {
        userAttributeRepo.delete(getUserAttribute(attributeKey));
    }

    private UserAttributeEntity getUserAttribute(String attributeKey) {
        return userAttributeRepo.findById(attributeKey)
                .orElseThrow(() -> new ResourceNotFoundException("User attribute not found: " + attributeKey));
    }
}
