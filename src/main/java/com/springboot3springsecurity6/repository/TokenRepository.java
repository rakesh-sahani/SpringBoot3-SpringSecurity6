package com.springboot3springsecurity6.repository;


import com.springboot3springsecurity6.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {
    Optional<TokenEntity> findByToken(String token);

    List<TokenEntity> findByMasterUserId_IdAndRevokedFalseOrExpiredFalse(UUID id);
}