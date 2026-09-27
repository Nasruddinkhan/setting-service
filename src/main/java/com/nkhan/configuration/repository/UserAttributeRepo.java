package com.nkhan.configuration.repository;

import com.nkhan.configuration.model.UserAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttributeRepo extends JpaRepository<UserAttributeEntity, String> {
}
