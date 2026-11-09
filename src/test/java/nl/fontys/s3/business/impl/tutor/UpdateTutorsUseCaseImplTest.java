package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.business.exception.InvalidTutorException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTutorsUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;
    @Mock
    private LanguageRepository languageRepositoryMock;

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
                .languages(List.of(new LanguageEntity()))
                .build();

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(1L);

        when(tutorRepositoryMock.findById(1L)).thenReturn(existingTutor);

        when(languageRepositoryMock.findAll()).thenReturn(List.of(languageEntity));

        updateTutorUseCase.updateTutor(existingTutor.getId(), request);

        verify(tutorRepositoryMock).save(existingTutor);
    }


    @Test
    void updateTutor_tutorNotFound_shouldThrowException() {
        long tutorId = 1L;
        UpdateTutorRequest request = UpdateTutorRequest.builder().name("Jane Doe").languageIds(List.of(1L)).build();

        when(tutorRepositoryMock.findById(tutorId)).thenReturn(null);

        assertThrows(InvalidTutorException.class, () -> updateTutorUseCase.updateTutor(tutorId, request));

        verify(tutorRepositoryMock, never()).save(any());
    }
}
