package nl.fontys.s3.configuration.security;

import nl.fontys.s3.configuration.security.auth.AuthenticationRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private AuthenticationRequestFilter authenticationRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filterChain() throws Exception {
        HttpSecurity mockHttpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(mockHttpSecurity.csrf(any(Customizer.class))).thenReturn(mockHttpSecurity);
        when(mockHttpSecurity.formLogin(any(Customizer.class))).thenReturn(mockHttpSecurity);
        when(mockHttpSecurity.sessionManagement(any(Customizer.class))).thenReturn(mockHttpSecurity);
        when(mockHttpSecurity.authorizeHttpRequests(any(Customizer.class))).thenReturn(mockHttpSecurity);
        when(mockHttpSecurity.exceptionHandling(any(Customizer.class))).thenReturn(mockHttpSecurity);
        when(mockHttpSecurity.addFilterBefore(any(), any())).thenReturn(mockHttpSecurity);

        SecurityFilterChain securityFilterChain = webSecurityConfig.filterChain(mockHttpSecurity, authenticationEntryPoint, authenticationRequestFilter);

        assertNotNull(securityFilterChain);
        verify(mockHttpSecurity).csrf(any(Customizer.class));
        verify(mockHttpSecurity).formLogin(any(Customizer.class));
        verify(mockHttpSecurity).sessionManagement(any(Customizer.class));
        verify(mockHttpSecurity).authorizeHttpRequests(any(Customizer.class));
        verify(mockHttpSecurity).exceptionHandling(any(Customizer.class));
        verify(mockHttpSecurity).addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    void corsConfigurer() {
        WebMvcConfigurer webMvcConfigurer = webSecurityConfig.corsConfigurer();

        assertNotNull(webMvcConfigurer);
        CorsRegistry corsRegistry = mock(CorsRegistry.class);
        CorsRegistration corsRegistration = mock(CorsRegistration.class);

        doReturn(corsRegistration).when(corsRegistry).addMapping("/**");
        doReturn(corsRegistration).when(corsRegistration).allowedOrigins("http://localhost:5173");
        doReturn(corsRegistration).when(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        doReturn(corsRegistration).when(corsRegistration).allowedHeaders("*");
        doReturn(corsRegistration).when(corsRegistration).allowCredentials(true);

        webMvcConfigurer.addCorsMappings(corsRegistry);

        verify(corsRegistry).addMapping("/**");
        verify(corsRegistration).allowedOrigins("http://localhost:5173");
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration).allowedHeaders("*");
        verify(corsRegistration).allowCredentials(true);
    }
}
