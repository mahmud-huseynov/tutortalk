package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.domain.learner.GetLearnersResponse;
import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.LearnerRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLearnersUseCaseImplTest {

    @Mock
    private LearnerRepository learnerRepositoryMock;

    @InjectMocks
    private GetLearnersUseCaseImpl getLearnersUseCase;

    @Test
    void getLearners_shouldReturnListOfLearners() {
        LanguageEntity english = LanguageEntity.builder().id(1L).name("English").code("EN").build();
        LanguageEntity dutch = LanguageEntity.builder().id(2L).name("Dutch").code("NL").build();
        List<LanguageEntity> languages = new ArrayList<>();
        languages.add(dutch);
        languages.add(english);

        TutorEntity tutorEntity1 = TutorEntity.builder()
                .id(1L)
                .name("Tutor 1")
                .languages(languages)
                .build();

        TutorEntity tutorEntity2 = TutorEntity.builder()
                .id(2L)
                .name("Tutor 2")
                .languages(languages)
                .build();

        LearnerEntity entity1 = LearnerEntity.builder()
                .id(1L)
                .name("Alice")
                .language(english)
                .tutor(tutorEntity1)
                .build();

        LearnerEntity entity2 = LearnerEntity.builder()
                .id(2L)
                .name("Bob")
                .language(dutch)
                .tutor(tutorEntity2)
                .build();

        when(learnerRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        GetLearnersResponse actualResult = getLearnersUseCase.getLearners();

        Learner learner1 = Learner.builder().id(1L).name("Alice").language("English").tutor("Tutor 1").language("English").build();
        Learner learner2 = Learner.builder().id(2L).name("Bob").language("Dutch").tutor("Tutor 2").build();
        GetLearnersResponse expectedResult = GetLearnersResponse.builder().learners(List.of(learner1, learner2)).build();
        assertEquals(expectedResult, actualResult);

        verify(learnerRepositoryMock).findAll();
    }
}
