package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.business.exception.InvalidLearnerException;
import nl.fontys.s3.domain.learner.UpdateLearnerRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.LearnerRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateLearnerUseCaseImplTest {

    @Mock
    private LearnerRepository learnerRepositoryMock;
    @Mock
    private TutorRepository tutorRepositoryMock;
    @Mock
    private LanguageRepository languageRepositoryMock;

    @InjectMocks
    private UpdateLearnerUseCaseImpl updateLearnerUseCase;

    @Test
    void updateLearner_learnerFound_shouldUpdate() {
        Long id = 1L;
        UpdateLearnerRequest request = UpdateLearnerRequest.builder()
                .name("New Name")
                .languageId(1L)
                .tutorId(1L)
                .build();

        LearnerEntity existingLearner = LearnerEntity.builder()
                .id(1L)
                .name("Old Name")
                .language(new LanguageEntity())
                .tutor(new TutorEntity())
                .build();

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(1L);

        TutorEntity tutorEntity = new TutorEntity();
        tutorEntity.setId(1L);

        when(learnerRepositoryMock.findById(1L)).thenReturn(existingLearner);

        when(languageRepositoryMock.findById(id)).thenReturn(Optional.of(languageEntity));

        when(tutorRepositoryMock.findById(id)).thenReturn(Optional.of(tutorEntity));

        updateLearnerUseCase.updateLearner(existingLearner.getId(), request);

        verify(learnerRepositoryMock).save(existingLearner);
    }




    @Test
    void updateLearner_learnerNotFound_shouldThrowException() {
        long learnerId = 1L;
        UpdateLearnerRequest request = UpdateLearnerRequest.builder().name("Alice").languageId(1L).tutorId(1L).build();

        when(learnerRepositoryMock.findById(learnerId)).thenReturn(null);

        assertThrows(InvalidLearnerException.class, () -> updateLearnerUseCase.updateLearner(learnerId, request));

        verify(learnerRepositoryMock, never()).save(any());
    }
}
