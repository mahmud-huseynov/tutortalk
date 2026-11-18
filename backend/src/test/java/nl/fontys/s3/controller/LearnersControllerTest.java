package nl.fontys.s3.controller;

import nl.fontys.s3.business.learner.*;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.domain.learner.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LearnersController.class)
@Import(WebSecurityConfig.class)
class LearnersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetLearnersUseCase getLearnersUseCase;

    @MockBean
    private GetLearnerUseCase getLearnerUseCase;

    @MockBean
    private CreateLearnerUseCase createLearnerUseCase;

    @MockBean
    private UpdateLearnerUseCase updateLearnerUseCase;

    @MockBean
    private DeleteLearnerUseCase deleteLearnerUseCase;

    @MockBean
    private LearnerStatisticsUseCase learnerStatisticsUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLearners_ReturnsListOfLearners() throws Exception {
        GetLearnersResponse response = new GetLearnersResponse();
        response.setLearners(List.of(
                new Learner(1L, "John Doe", "Dutch", "Mahmud"),
                new Learner(2L, "Jane Smith", "English", "Frank")
        ));

        when(getLearnersUseCase.getLearners()).thenReturn(response);

        mockMvc.perform(get("/learners"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"learners":[{"id":1,"name":"John Doe","language":"Dutch","tutor":"Mahmud"},{"id":2,"name":"Jane Smith","language":"English","tutor":"Frank"}]}
                """));
        verify(getLearnersUseCase).getLearners();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLearner_ReturnsLearnerById() throws Exception {
        long learnerId = 1L;
        Learner learner = new Learner(learnerId, "John Doe", "Dutch", "Frank");
        GetLearnerResponse response = new GetLearnerResponse(learner);

        when(getLearnerUseCase.getLearnerById(learnerId)).thenReturn(response);

        mockMvc.perform(get("/learners/{id}", learnerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"id":1,"name":"John Doe","language":"Dutch","tutor":"Frank"}
                """));
        verify(getLearnerUseCase).getLearnerById(learnerId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLearner_ReturnsNotFoundForInvalidLearnerId() throws Exception {
        long learnerId = 999L;
        GetLearnerResponse response = new GetLearnerResponse(null);

        when(getLearnerUseCase.getLearnerById(learnerId)).thenReturn(response);

        mockMvc.perform(get("/learners/{id}", learnerId))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(getLearnerUseCase).getLearnerById(learnerId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createLearner_ReturnsCreated() throws Exception {
        CreateLearnerRequest request = new CreateLearnerRequest("John Doe", 1L, 2L);
        CreateLearnerResponse response = new CreateLearnerResponse(1L);

        when(createLearnerUseCase.createLearner(request)).thenReturn(response);

        mockMvc.perform(post("/learners")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"name":"John Doe","languageId":1,"tutorId":2}
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"learnerId":1}
                """));
        verify(createLearnerUseCase).createLearner(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateLearner_ReturnsNoContent() throws Exception {
        long learnerId = 1L;
        UpdateLearnerRequest request = new UpdateLearnerRequest("John Doe", 1L, 2L);

        mockMvc.perform(put("/learners/{id}", learnerId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"name":"John Doe","languageId":1,"tutorId":2}
                    """))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(updateLearnerUseCase).updateLearner(learnerId, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLearner_ReturnsNoContent() throws Exception {
        long learnerId = 1L;

        mockMvc.perform(delete("/learners/{id}", learnerId))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(deleteLearnerUseCase).deleteLearner(learnerId);
    }
}
