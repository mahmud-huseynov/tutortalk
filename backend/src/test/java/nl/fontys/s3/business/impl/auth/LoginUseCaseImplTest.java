package nl.fontys.s3.business.impl.auth;

import nl.fontys.s3.business.exception.InvalidCredentialsException;
import nl.fontys.s3.business.auth.RefreshTokenService;
import nl.fontys.s3.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.domain.auth.LoginRequest;
import nl.fontys.s3.domain.auth.LoginResponse;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.UserEntity;
import nl.fontys.s3.persistence.entity.UserRoleEntity;
import nl.fontys.s3.persistence.security.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private AccessTokenEncoder accessTokenEncoderMock;
    @Mock
    private RefreshTokenService refreshTokenServiceMock;
    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void login_validCredentials_shouldReturnLoginResponse() {
        LoginRequest request = LoginRequest.builder()
                .username("validUser")
                .password("validPassword")
                .build();

        UserEntity user = UserEntity.builder()
                .username("validUser")
                .password("encodedPassword")
                .userRoles(Set.of(UserRoleEntity.builder().role(RoleEnum.ADMIN).build()))
                .build();

        when(userRepositoryMock.findByUsername("validUser")).thenReturn(user);
        when(passwordEncoderMock.matches("validPassword", "encodedPassword")).thenReturn(true);
        when(accessTokenEncoderMock.encode(any(AccessTokenImpl.class))).thenReturn("accessToken");
        when(refreshTokenServiceMock.generateRefreshToken(user)).thenReturn("refreshToken");

        LoginResponse response = loginUseCase.login(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void login_invalidUsername_shouldThrowInvalidCredentialsException() {
        LoginRequest request = LoginRequest.builder()
                .username("invalidUser")
                .password("anyPassword")
                .build();

        when(userRepositoryMock.findByUsername("invalidUser")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(request));
    }

    @Test
    void login_invalidPassword_shouldThrowInvalidCredentialsException() {
        LoginRequest request = LoginRequest.builder()
                .username("validUser")
                .password("invalidPassword")
                .build();

        UserEntity user = UserEntity.builder()
                .username("validUser")
                .password("encodedPassword")
                .userRoles(Set.of(UserRoleEntity.builder().role(RoleEnum.ADMIN).build()))
                .build();

        when(userRepositoryMock.findByUsername("validUser")).thenReturn(user);
        when(passwordEncoderMock.matches("invalidPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(request));
    }

    @Test
    void generateAccessToken_shouldReturnEncodedToken() {
        UserEntity user = UserEntity.builder()
                .username("validUser")
                .userRoles(Set.of(UserRoleEntity.builder().role(RoleEnum.ADMIN).build()))
                .build();

        when(accessTokenEncoderMock.encode(any(AccessTokenImpl.class))).thenReturn("encodedToken");

        String token = loginUseCase.generateAccessToken(user);

        assertNotNull(token);
        assertEquals("encodedToken", token);
    }
}
