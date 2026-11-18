package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.UserEntity;
import nl.fontys.s3.persistence.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
    RefreshToken findByUserAndExpiresAtAfter(UserEntity user, Instant expiresAt);
}