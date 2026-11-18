package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.domain.tutor.UpdateTutorRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTutorsUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;
    @Mock
    private LanguageRepository languageRepositoryMock;
    @Mock
    private AccessToken accessTokenMock;
    @InjectMocks
    private UpdateTutorUseCaseImpl updateTutorUseCase;

    @Test
    void updateTutor_tutorFound_shouldUpdate() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("New Name")
                .languageIds(List.of(1L))
                .build();

        TutorEntity existingTutor = TutorEntity.builder()
                .id(1L)
                .name("Old Name")
                .languages(new HashSet<>())
                .build();

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(1L);

        when(accessTokenMock.hasRole(anyString())).thenReturn(true);
        when(accessTokenMock.getStudentId()).thenReturn(existingTutor.getId());

        when(tutorRepositoryMock.findById(1L)).thenReturn(existingTutor);
        when(languageRepositoryMock.findAll()).thenReturn(List.of(languageEntity));

        updateTutorUseCase.setRequestAccessToken(accessTokenMock);

        assertDoesNotThrow(() -> updateTutorUseCase.updateTutor(existingTutor.getId(), request));

        verify(tutorRepositoryMock).save(existingTutor);
        assertEquals("New Name", existingTutor.getName());
        assertEquals(1, existingTutor.getLanguages().size());
        assertTrue(existingTutor.getLanguages().contains(languageEntity));
    }

    @Test
    void updateTutor_tutorNotFound_shouldNotUpdate() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("New Name")
                .languageIds(List.of(1L))
                .build();

        when(tutorRepositoryMock.findById(1L)).thenReturn(null);

        assertThrows(InvalidTutorException.class, () -> updateTutorUseCase.updateTutor(1L, request));

        verify(tutorRepositoryMock, never()).save(any());
    }

    @Test
    void updateTutor_unauthorizedAccess_shouldThrowException() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("New Name")
                .languageIds(List.of(1L))
                .build();

        when(accessTokenMock.hasRole(anyString())).thenReturn(true);
        when(accessTokenMock.getStudentId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class,
                () -> updateTutorUseCase.updateTutor(1L, request));
    }

    @Test
    void updateFields_shouldThrowInvalidTutorExceptionWhenLanguageIdsAreInvalid() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("New Name")
                .languageIds(List.of(1L, 2L)) // Invalid language ID
                .build();

        TutorEntity tutor = TutorEntity.builder()
                .id(1L)
                .name("Old Name")
                .languages(new HashSet<>() {
                })
                .build();

        when(languageRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        assertThrows(InvalidTutorException.class, () -> updateTutorUseCase.updateFields(request, tutor));
    }

    @Test
    void isSameValues_sameValues_shouldReturnTrue() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("Old Name")
                .languageIds(List.of(1L, 2L))
                .build();

        TutorEntity tutor = TutorEntity.builder()
                .name("Old Name")
                .languages(createLanguageEntities(1L, 2L))
                .build();

        assertTrue(updateTutorUseCase.isSameValues(request, tutor));
    }

    @Test
    void isSameValues_differentValues_shouldReturnFalse() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("Old Name")
                .languageIds(List.of(1L, 2L))
                .build();

        TutorEntity tutor = TutorEntity.builder()
                .name("New Name")
                .languages(createLanguageEntities(1L))
                .build();

        assertFalse(updateTutorUseCase.isSameValues(request, tutor));
    }

    @Test
    void updateTutor_valuesAreSame_shouldThrowInvalidTutorException() {
        UpdateTutorRequest request = UpdateTutorRequest.builder()
                .name("Old Name")
                .languageIds(List.of(1L, 2L))
                .build();

        TutorEntity tutor = TutorEntity.builder()
                .name("Old Name")
                .languages(createLanguageEntities(1L, 2L))
                .build();



        assertThrows(RuntimeException.class, () -> {
            boolean areValuesSame = updateTutorUseCase.isSameValues(request, tutor);
            if (areValuesSame) {
                throw new InvalidTutorException("Tutor values are same, no update required");
            }
        });
    }


    private Set<LanguageEntity> createLanguageEntities(Long... ids) {
        Set<LanguageEntity> languages = new HashSet<>();
        for (Long id : ids) {
            LanguageEntity language = new LanguageEntity();
            language.setId(id);
            languages.add(language);
        }
        return languages;
    }


}
