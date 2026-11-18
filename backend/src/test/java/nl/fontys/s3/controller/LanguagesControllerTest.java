package nl.fontys.s3.controller;

import nl.fontys.s3.business.language.*;
import nl.fontys.s3.configuration.security.WebSecurityConfig;
import nl.fontys.s3.domain.language.*;
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
@WebMvcTest(LanguagesController.class)
@Import(WebSecurityConfig.class)
class LanguagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetLanguagesUseCase getLanguagesUseCase;

    @MockBean
    private GetLanguageUseCase getLanguageUseCase;

    @MockBean
    private CreateLanguageUseCase createLanguageUseCase;

    @MockBean
    private UpdateLanguageUseCase updateLanguageUseCase;

    @MockBean
    private DeleteLanguageUseCase deleteLanguageUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Test
    @WithMockUser
    void getLanguages_ReturnsListOfLanguages() throws Exception {
        GetLanguagesResponse response = new GetLanguagesResponse();
        response.setLanguages(List.of(
                new Language(1L, "English", "EN"),
                new Language(2L, "Spanish", "ES")
        ));

        when(getLanguagesUseCase.getLanguages()).thenReturn(response);

        mockMvc.perform(get("/languages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"languages":[{"id":1,"name":"English","code":"EN"},{"id":2,"name":"Spanish","code":"ES"}]}
                """));
        verify(getLanguagesUseCase).getLanguages();
    }

    @Test
    @WithMockUser
    void getLanguage_ReturnsLanguageById() throws Exception {
        long languageId = 1L;
        Language language = new Language(languageId, "English", "EN");
        GetLanguageResponse response = new GetLanguageResponse(language);

        when(getLanguageUseCase.getLanguageById(languageId)).thenReturn(response);

        mockMvc.perform(get("/languages/{id}", languageId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"id":1,"name":"English","code":"EN"}
                """));
        verify(getLanguageUseCase).getLanguageById(languageId);
    }

    @Test
    @WithMockUser
    void getLanguage_ReturnsNotFoundForInvalidLanguageId() throws Exception {
        long languageId = 999L;
        GetLanguageResponse response = new GetLanguageResponse(null);

        when(getLanguageUseCase.getLanguageById(languageId)).thenReturn(response);

        mockMvc.perform(get("/languages/{id}", languageId))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(getLanguageUseCase).getLanguageById(languageId);
    }

    @Test
    @WithMockUser
    void createLanguage_ReturnsCreated() throws Exception {
        CreateLanguageRequest request = new CreateLanguageRequest("French", "FR");
        CreateLanguageResponse response = new CreateLanguageResponse(1L);

        when(createLanguageUseCase.createLanguage(request)).thenReturn(response);

        mockMvc.perform(post("/languages")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"name":"French","code":"FR"}
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                {"languageId":1}
                """));
        verify(createLanguageUseCase).createLanguage(request);
    }

    @Test
    @WithMockUser
    void updateLanguage_ReturnsNoContent() throws Exception {
        long languageId = 1L;
        UpdateLanguageRequest request = new UpdateLanguageRequest("German", "GR");

        mockMvc.perform(put("/languages/{id}", languageId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                    {"name":"German","code":"GR"}
                    """))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(updateLanguageUseCase).updateLanguage(languageId, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLanguage_ReturnsNoContent() throws Exception {
        long languageId = 1L;

        mockMvc.perform(delete("/languages/{id}", languageId))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(deleteLanguageUseCase).deleteLanguage(languageId);
    }
}
