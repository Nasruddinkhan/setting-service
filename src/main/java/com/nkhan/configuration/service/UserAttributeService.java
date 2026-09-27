package com.nkhan.configuration.service;

import com.nkhan.configuration.model.UserAttributeEntity;

import java.util.List;

public interface UserAttributeService {
    List<UserAttributeEntity> findAllUserAttributes();

    UserAttributeEntity findUserAttributeById(String attributeKey);

    UserAttributeEntity createUserAttribute(UserAttributeEntity req);

    UserAttributeEntity updateUserAttribute(String attributeKey, UserAttributeEntity req);

    void deleteUserAttribute(String attributeKey);
}
