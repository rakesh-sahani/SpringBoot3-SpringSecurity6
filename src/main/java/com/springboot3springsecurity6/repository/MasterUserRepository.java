package com.springboot3springsecurity6.repository;


import com.springboot3springsecurity6.entities.MasterUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MasterUserRepository extends JpaRepository<MasterUserEntity, UUID> {
    Optional<MasterUserEntity> findByEmail(String email);
}