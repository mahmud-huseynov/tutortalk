package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.domain.learner.CreateLearnerRequest;
import nl.fontys.s3.domain.learner.CreateLearnerResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateLearnerUseCaseImplTest {

    @Mock
    private LearnerRepository learnerRepositoryMock;

    @Mock
    private TutorRepository tutorRepositoryMock;

    @Mock
    private LanguageRepository languageRepositoryMock;

    @Mock
    private LearnerValidatorImpl learnerValidatorMock;

    @InjectMocks
    private CreateLearnerUseCaseImpl createLearnerUseCase;

    @Test
    void createLearner_shouldReturnNewLearner() {
        CreateLearnerRequest request = CreateLearnerRequest.builder()
                .languageId(1L)
                .tutorId(1L)
                .build();

        when(languageRepositoryMock.findById(request.getLanguageId()))
                .thenReturn(Optional.of(LanguageEntity.builder().id(1L).code("EN").name("English").build()));

        when(tutorRepositoryMock.findById(request.getTutorId()))
                .thenReturn(Optional.of(TutorEntity.builder().id(1L).name("Test Tutor").build()));

        when(learnerRepositoryMock.save(any(LearnerEntity.class)))
                .thenAnswer(invocation -> {
                    LearnerEntity entityArgument = invocation.getArgument(0);
                    entityArgument.setId(1L);
                    return entityArgument;
                });

        CreateLearnerResponse response = createLearnerUseCase.createLearner(request);

        assertNotNull(response);
        assertEquals(1L, response.getLearnerId());

        verify(languageRepositoryMock).findById(request.getLanguageId());
        verify(tutorRepositoryMock).findById(request.getTutorId());
        verify(learnerRepositoryMock).save(any(LearnerEntity.class));
    }
}
