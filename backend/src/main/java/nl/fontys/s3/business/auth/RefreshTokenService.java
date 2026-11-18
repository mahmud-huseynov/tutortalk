package nl.fontys.s3.business.auth;

import nl.fontys.s3.persistence.entity.UserEntity;

public interface RefreshTokenService {
    String generateRefreshToken(UserEntity user);
    boolean validateRefreshToken(String refreshToken);
    UserEntity getUserByRefreshToken(String refreshToken);
}