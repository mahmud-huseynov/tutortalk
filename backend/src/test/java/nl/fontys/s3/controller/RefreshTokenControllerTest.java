package nl.fontys.s3.controller;

import nl.fontys.s3.business.auth.RefreshTokenService;
import nl.fontys.s3.business.impl.auth.LoginUseCaseImpl;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RefreshTokenController.class)
@Import(WebSecurityConfig.class)
class RefreshTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private LoginUseCaseImpl loginUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser
    void refreshAccessToken_ValidRefreshToken_ReturnsNewAccessToken() throws Exception {
        String refreshToken = "valid_refresh_token";
        UserEntity userEntity = new UserEntity();

        when(refreshTokenService.validateRefreshToken(refreshToken)).thenReturn(true);
        when(refreshTokenService.getUserByRefreshToken(refreshToken)).thenReturn(userEntity);
        when(loginUseCase.generateAccessToken(userEntity)).thenReturn("new_access_token");

        mockMvc.perform(post("/refresh_tokens/refresh?refreshToken={refreshToken}", refreshToken)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"accessToken":"new_access_token"}
                """));

        verify(refreshTokenService).validateRefreshToken(refreshToken);
        verify(refreshTokenService).getUserByRefreshToken(refreshToken);
        verify(loginUseCase).generateAccessToken(userEntity);
    }

    @Test
    @WithMockUser
    void refreshAccessToken_InvalidRefreshToken_ReturnsBadRequest() throws Exception {
        String refreshToken = "invalid_refresh_token";

        when(refreshTokenService.validateRefreshToken(refreshToken)).thenReturn(false);

        mockMvc.perform(post("/refresh_tokens/refresh?refreshToken={refreshToken}", refreshToken)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(refreshTokenService).validateRefreshToken(refreshToken);
    }


    @Test
    @WithMockUser
    void validateRefreshToken_ValidRefreshToken_ReturnsTrue() throws Exception {
        String refreshToken = "valid_refresh_token";

        when(refreshTokenService.validateRefreshToken(refreshToken)).thenReturn(true);

        mockMvc.perform(post("/refresh_tokens/validate?refreshToken={refreshToken}", refreshToken)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"isValid":true}
                """));

        verify(refreshTokenService).validateRefreshToken(refreshToken);
    }

    @Test
    @WithMockUser
    void validateRefreshToken_InvalidRefreshToken_ReturnsFalse() throws Exception {
        String refreshToken = "invalid_refresh_token";

        when(refreshTokenService.validateRefreshToken(refreshToken)).thenReturn(false);

        mockMvc.perform(post("/refresh_tokens/validate?refreshToken={refreshToken}", refreshToken)
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"isValid":false}
                """));

        verify(refreshTokenService).validateRefreshToken(refreshToken);
    }
}
