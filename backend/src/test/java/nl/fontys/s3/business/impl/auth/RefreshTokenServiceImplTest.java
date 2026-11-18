package nl.fontys.s3.business.impl.auth;

import nl.fontys.s3.persistence.RefreshTokenRepository;
import nl.fontys.s3.persistence.entity.UserEntity;
import nl.fontys.s3.persistence.security.RefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepositoryMock;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void generateRefreshToken_existingToken_shouldReturnExistingToken() {
        UserEntity user = UserEntity.builder().id(1L).build();
        String existingToken = UUID.randomUUID().toString();
        RefreshToken existingRefreshToken = new RefreshToken();
        existingRefreshToken.setToken(existingToken);
        existingRefreshToken.setUser(user);
        existingRefreshToken.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        when(refreshTokenRepositoryMock.findByUserAndExpiresAtAfter(eq(user), any(Instant.class)))
                .thenReturn(existingRefreshToken);

        String token = refreshTokenService.generateRefreshToken(user);

        assertNotNull(token);
        assertEquals(existingToken, token);
        verify(refreshTokenRepositoryMock, never()).save(any(RefreshToken.class));
    }

    @Test
    void generateRefreshToken_noExistingToken_shouldGenerateNewToken() {
        UserEntity user = UserEntity.builder().id(1L).build();

        when(refreshTokenRepositoryMock.findByUserAndExpiresAtAfter(eq(user), any(Instant.class)))
                .thenReturn(null);

        String token = refreshTokenService.generateRefreshToken(user);

        assertNotNull(token);
        verify(refreshTokenRepositoryMock).save(any(RefreshToken.class));
    }

    @Test
    void validateRefreshToken_validToken_shouldReturnTrue() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        when(refreshTokenRepositoryMock.findByToken(token)).thenReturn(refreshTokenEntity);

        boolean isValid = refreshTokenService.validateRefreshToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateRefreshToken_expiredToken_shouldReturnFalse() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setExpiresAt(Instant.now().minus(1, ChronoUnit.DAYS));

        when(refreshTokenRepositoryMock.findByToken(token)).thenReturn(refreshTokenEntity);

        boolean isValid = refreshTokenService.validateRefreshToken(token);

        assertFalse(isValid);
    }

    @Test
    void validateRefreshToken_nonExistentToken_shouldReturnFalse() {
        String token = UUID.randomUUID().toString();

        when(refreshTokenRepositoryMock.findByToken(token)).thenReturn(null);

        boolean isValid = refreshTokenService.validateRefreshToken(token);

        assertFalse(isValid);
    }

    @Test
    void getUserByRefreshToken_validToken_shouldReturnUser() {
        String token = UUID.randomUUID().toString();
        UserEntity user = UserEntity.builder().id(1L).build();
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        when(refreshTokenRepositoryMock.findByToken(token)).thenReturn(refreshTokenEntity);

        UserEntity result = refreshTokenService.getUserByRefreshToken(token);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getUserByRefreshToken_nonExistentToken_shouldReturnNull() {
        String token = UUID.randomUUID().toString();

        when(refreshTokenRepositoryMock.findByToken(token)).thenReturn(null);

        UserEntity result = refreshTokenService.getUserByRefreshToken(token);

        assertNull(result);
    }
}
