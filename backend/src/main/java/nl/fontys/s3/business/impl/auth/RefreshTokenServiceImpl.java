package nl.fontys.s3.business.impl.auth;

import nl.fontys.s3.business.auth.RefreshTokenService;
import nl.fontys.s3.persistence.RefreshTokenRepository;
import nl.fontys.s3.persistence.entity.UserEntity;
import nl.fontys.s3.persistence.security.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateRefreshToken(UserEntity user) {

        RefreshToken existingRefreshToken = refreshTokenRepository.findByUserAndExpiresAtAfter(user, Instant.now());

        if (existingRefreshToken != null) {
            return existingRefreshToken.getToken();
        }
        String token = UUID.randomUUID().toString();

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        refreshTokenRepository.save(refreshTokenEntity);

        return token;
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);

        return refreshTokenEntity != null && refreshTokenEntity.getExpiresAt().isAfter(Instant.now());
    }

    @Override
    public UserEntity getUserByRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);

        return refreshTokenEntity != null ? refreshTokenEntity.getUser() : null;
    }

}
