package nl.fontys.s3.configuration.security.auth;

import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.configuration.security.token.impl.AccessTokenImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RequestAuthenticatedUserProviderTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAuthenticatedUserInRequest_noSecurityContext_shouldReturnNull() {
        SecurityContextHolder.clearContext();

        AccessToken result = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        assertNull(result);
    }

    @Test
    void getAuthenticatedUserInRequest_noAuthentication_shouldReturnNull() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        AccessToken result = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        assertNull(result);
    }

    @Test
    void getAuthenticatedUserInRequest_noAccessTokenDetails_shouldReturnNull() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(new Object());
        SecurityContextHolder.setContext(securityContext);

        AccessToken result = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        assertNull(result);
    }

    @Test
    void getAuthenticatedUserInRequest_withAccessTokenDetails_shouldReturnAccessToken() {
        AccessToken accessToken = new AccessTokenImpl("user", 123L, List.of("ROLE_USER"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(accessToken);
        SecurityContextHolder.setContext(securityContext);

        AccessToken result = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        assertNotNull(result);
        assertEquals(accessToken, result);
    }
}
