package nl.fontys.s3.controller;

import nl.fontys.s3.business.auth.LoginUseCase;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.domain.auth.LoginRequest;
import nl.fontys.s3.domain.auth.LoginResponse;
import nl.fontys.s3.configuration.security.token.AccessTokenDecoder;
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
@WebMvcTest(LoginController.class)
@Import(WebSecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser
    void login_ValidLoginRequest_ReturnsLoginResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse loginResponse = new LoginResponse("access_token", "refresh_token");

        when(loginUseCase.login(loginRequest)).thenReturn(loginResponse);

        mockMvc.perform(post("/tokens")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"username":"username","password":"password"}
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"accessToken":"access_token","refreshToken":"refresh_token"}
                """));
        verify(loginUseCase).login(loginRequest);
    }
}
