package nl.fontys.s3.business.impl.auth;

import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidCredentialsException;
import nl.fontys.s3.business.auth.LoginUseCase;
import nl.fontys.s3.business.auth.RefreshTokenService;
import nl.fontys.s3.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.domain.auth.LoginRequest;
import nl.fontys.s3.domain.auth.LoginResponse;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateAccessToken(UserEntity user) {
        Long studentId = user.getTutor() != null ? user.getTutor().getId() : null;
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getUsername(), studentId, roles));
    }

}
