package nl.fontys.s3.configuration.security.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.configuration.security.token.exception.InvalidAccessTokenException;
import nl.fontys.s3.configuration.security.token.impl.AccessTokenImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationRequestFilterTest {

    @Mock
    private AccessTokenDecoder accessTokenDecoder;

    @InjectMocks
    private AuthenticationRequestFilter authenticationRequestFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_shouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authenticationRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_invalidAuthorizationHeader_shouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        authenticationRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validAccessToken_shouldSetSecurityContext() throws ServletException, IOException {
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        AccessToken accessToken = new AccessTokenImpl("user", 123L, List.of("USER"));
        when(accessTokenDecoder.decode(token)).thenReturn(accessToken);

        authenticationRequestFilter.doFilterInternal(request, response, chain);

        verify(accessTokenDecoder).decode(token);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertEquals("user", user.getUsername());
    }

    @Test
    void doFilterInternal_invalidAccessToken_shouldSendUnauthorizedError() throws ServletException, IOException {
        String token = "invalidToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(accessTokenDecoder.decode(token)).thenThrow(new InvalidAccessTokenException("Invalid token"));

        authenticationRequestFilter.doFilterInternal(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).flushBuffer();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
