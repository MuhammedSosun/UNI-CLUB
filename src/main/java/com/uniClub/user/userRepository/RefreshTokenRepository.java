package com.uniClub.repository.userRepository;

import com.uniClub.entity.userEntity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUserId(UUID userId);
    List<RefreshToken> findAllByUserId(UUID userId);
    void deleteAllByUserId(UUID userId);
}
