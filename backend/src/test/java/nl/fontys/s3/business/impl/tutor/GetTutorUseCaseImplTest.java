package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.business.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.domain.tutor.GetTutorResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.TutorEntity;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTutorUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;
    @Mock
    private AccessToken accessTokenMock;
    @InjectMocks
    private GetTutorUseCaseImpl getTutorUseCase;

    @Test
    void getTutorById_shouldReturnTutorConverted() {

        getTutorUseCase.setRequestAccessToken(accessTokenMock);

        LanguageEntity englishLanguageEntity = LanguageEntity.builder().id(1L).code("EN").name("English").build();
        Long id = 1L;

        TutorEntity tutorEntity = TutorEntity.builder()
                .id(id)
                .name("John Doe")
                .languages(Collections.singleton(englishLanguageEntity))
                .build();

        when(tutorRepositoryMock.findById(id)).thenReturn(Optional.of(tutorEntity));

        Tutor expectedTutor = Tutor.builder()
                .id(id)
                .name("John Doe")
                .languages(englishLanguageEntity.getName())
                .build();
        GetTutorResponse expectedResponse = GetTutorResponse.builder()
                .tutor(expectedTutor)
                .build();

        GetTutorResponse actualResponse = getTutorUseCase.getTutorById(id);

        assertEquals(expectedResponse, actualResponse);

        verify(tutorRepositoryMock).findById(id);
    }


    @Test
    void getTutorById_shouldThrowIllegalArgumentExceptionWhenAccessTokenIsNull() {
        Long id = 1L;

        getTutorUseCase.setRequestAccessToken(null);

        assertThrows(IllegalArgumentException.class, () -> getTutorUseCase.getTutorById(id));
    }

    @Test
    void getTutorById_shouldThrowUnauthorizedDataAccessExceptionWhenRoleMismatch() {
        Long id = 1L;

        AccessToken accessTokenMock = mock(AccessToken.class);

        getTutorUseCase.setRequestAccessToken(accessTokenMock);
        when(accessTokenMock.hasRole(anyString())).thenReturn(true); // Assuming role check fails

        assertThrows(UnauthorizedDataAccessException.class, () -> getTutorUseCase.getTutorById(id));
    }

    @Test
    void getTutorById_shouldReturnNullWhenTutorNotFound() {
        getTutorUseCase.setRequestAccessToken(accessTokenMock);

        Long id = 1L;

        when(tutorRepositoryMock.findById(id)).thenReturn(Optional.empty());

        assertNull(getTutorUseCase.getTutorById(id));

        verify(tutorRepositoryMock).findById(id);
    }

    @Test
    void getTutorById_shouldHandleNullLanguages() {
        getTutorUseCase.setRequestAccessToken(accessTokenMock);
        Long id = 1L;

        TutorEntity tutorEntity = TutorEntity.builder()
                .id(id)
                .name("John Doe")
                .languages(null)
                .build();

        when(tutorRepositoryMock.findById(id)).thenReturn(Optional.of(tutorEntity));

        Tutor expectedTutor = Tutor.builder()
                .id(id)
                .name("John Doe")
                .languages("")
                .build();
        GetTutorResponse expectedResponse = GetTutorResponse.builder()
                .tutor(expectedTutor)
                .build();

        GetTutorResponse actualResponse = getTutorUseCase.getTutorById(id);

        assertEquals(expectedResponse, actualResponse);

        verify(tutorRepositoryMock).findById(id);
    }

}
