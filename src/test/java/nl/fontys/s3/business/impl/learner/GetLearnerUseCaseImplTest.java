package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.domain.learner.GetLearnerResponse;
import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.LearnerRepository;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLearnerUseCaseImplTest {

    @Mock
    private LearnerRepository learnerRepositoryMock;

    @InjectMocks
    private GetLearnerUseCaseImpl getLearnerUseCase;

    @Test
    void getLearnerById_shouldReturnLearnerConverted() {
        Long id = 1L;
        LanguageEntity languageEntity = LanguageEntity.builder()
                .id(id)
                .name("English")
                .build();

        TutorEntity tutorEntity = TutorEntity.builder()
                .id(id)
                .name("Jacco Snoeren")
                .build();

        LearnerEntity learnerEntity = LearnerEntity.builder()
                .id(id)
                .name("Alice")
                .language(languageEntity)
                .tutor(tutorEntity)
                .build();

        when(learnerRepositoryMock.findById(id)).thenReturn(Optional.of(learnerEntity));

        GetLearnerResponse expectedResponse = GetLearnerResponse.builder()
                .learner(new Learner(1L, "Alice", "English", "Jacco Snoeren"))
                .build();

        GetLearnerResponse actualResponse = getLearnerUseCase.getLearnerById(1L);

        assertEquals(expectedResponse, actualResponse);
    }
}
