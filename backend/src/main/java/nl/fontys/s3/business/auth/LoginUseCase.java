package nl.fontys.s3.business.auth;

import nl.fontys.s3.domain.auth.LoginRequest;
import nl.fontys.s3.domain.auth.LoginResponse;
import nl.fontys.s3.persistence.entity.UserEntity;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
    String generateAccessToken(UserEntity user); // Add this method
}
