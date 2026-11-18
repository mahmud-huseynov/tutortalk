package nl.fontys.s3.controller;

import nl.fontys.s3.business.tutor.*;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.domain.tutor.*;
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
@WebMvcTest(TutorsController.class)
@Import(WebSecurityConfig.class)
class TutorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTutorsUseCase getTutorsUseCase;

    @MockBean
    private GetTutorUseCase getTutorUseCase;

    @MockBean
    private CreateTutorUseCase createTutorUseCase;

    @MockBean
    private UpdateTutorUseCase updateTutorUseCase;

    @MockBean
    private DeleteTutorUseCase deleteTutorUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTutors_ReturnsListOfTutors() throws Exception {
        GetTutorsResponse response = new GetTutorsResponse();
        response.setTutors(List.of(
                new Tutor(1L, "John Doe", "johndoe@gmail.com", "0612345678", "Dutch"),
                new Tutor(2L, "Jane Smith", "janesmith@gmail.com", "0612345678", "English")
        ));

        when(getTutorsUseCase.getTutors()).thenReturn(response);

        mockMvc.perform(get("/tutors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
    {"tutors":[{"id":1,"name":"John Doe","email":"johndoe@gmail.com","number":"0612345678","languages":"Dutch"},{"id":2,"name":"Jane Smith","email":"janesmith@gmail.com","number":"0612345678","languages":"English"}]}
    """));
        verify(getTutorsUseCase).getTutors();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTutor_ReturnsTutorById() throws Exception {
        long tutorId = 1L;
        Tutor tutor = new Tutor(tutorId, "John Doe", "johndoe@gmail.com", "0612345678", "Dutch");
        GetTutorResponse response = new GetTutorResponse(tutor);

        when(getTutorUseCase.getTutorById(tutorId)).thenReturn(response);

        mockMvc.perform(get("/tutors/{id}", tutorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"id":1,"name":"John Doe","email":"johndoe@gmail.com","number":"0612345678","languages":"Dutch"}
                """));
        verify(getTutorUseCase).getTutorById(tutorId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTutor_ReturnsNotFoundForInvalidTutorId() throws Exception {
        long tutorId = 999L;
        GetTutorResponse response = new GetTutorResponse(null);

        when(getTutorUseCase.getTutorById(tutorId)).thenReturn(response);

        mockMvc.perform(get("/tutors/{id}", tutorId))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(getTutorUseCase).getTutorById(tutorId);
    }

    @Test
    @WithMockUser
    void createTutor_ReturnsCreated() throws Exception {
        CreateTutorRequest request = new CreateTutorRequest("John Doe", List.of(1L, 2L));
        CreateTutorResponse response = new CreateTutorResponse(1L);

        when(createTutorUseCase.createTutor(request)).thenReturn(response);

        mockMvc.perform(post("/tutors")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
            {"name":"John Doe","languageIds":[1,2]}
            """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));

        verify(createTutorUseCase).createTutor(request);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
        void updateTutor_ReturnsNoContent() throws Exception {
        long tutorId = 1L;
        UpdateTutorRequest request = new UpdateTutorRequest("John Doe", List.of(1L, 2L));

        mockMvc.perform(put("/tutors/{id}", tutorId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"name":"John Doe","languageIds":[1,2]}
                    """))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(updateTutorUseCase).updateTutor(tutorId, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTutor_ReturnsNoContent() throws Exception {
        long tutorId = 1L;

        mockMvc.perform(delete("/tutors/{id}", tutorId))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(deleteTutorUseCase).deleteTutor(tutorId);
    }
}
