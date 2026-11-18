package nl.fontys.s3.controller;

import nl.fontys.s3.business.auth.SignupUseCase;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.domain.auth.SignupRequest;
import nl.fontys.s3.domain.auth.SignupResponse;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SignupController.class)
@Import(WebSecurityConfig.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupUseCase signupUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser
    void signupTutor_ReturnsCreated() throws Exception {
        SignupRequest request = new SignupRequest("username", "password", "email@example.com", "1234567890", List.of(1L, 2L), 1L);
        SignupResponse response = new SignupResponse("username", "Tutor registered successfully");

        when(signupUseCase.signupTutor(request)).thenReturn(response);

        mockMvc.perform(post("/signup/tutor")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"username":"username","password":"password","email":"email@example.com","number":"1234567890","languageIds":[1,2],"languageId":1}
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"username":"username","message":"Tutor registered successfully"}
                """));
        verify(signupUseCase).signupTutor(request);
    }

    @Test
    @WithMockUser
    void signupLearner_ReturnsCreated() throws Exception {
        SignupRequest request = new SignupRequest("username", "password", "email@example.com", "1234567890", List.of(1L, 2L), 1L);
        SignupResponse response = new SignupResponse("username", "Learner registered successfully");

        when(signupUseCase.signupLearner(request)).thenReturn(response);

        mockMvc.perform(post("/signup/learner")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"username":"username","password":"password","email":"email@example.com","number":"1234567890","languageIds":[1,2],"languageId":1}
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"username":"username","message":"Learner registered successfully"}
                """));
        verify(signupUseCase).signupLearner(request);
    }
}
