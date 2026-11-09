package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.domain.tutor.GetTutorsResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTutorsUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;

    @InjectMocks
    private GetTutorsUseCaseImpl getTutorsUseCase;

    @Test
    void getTutors_shouldReturnListOfTutors() {

        LanguageEntity englishLanguageEntity = LanguageEntity.builder().id(1L).code("EN").name("English").build();
        LanguageEntity dutchLanguageEntity = LanguageEntity.builder().id(2L).code("NL").name("Dutch").build();
        TutorEntity entity1 =
                TutorEntity.builder().id(1L).name("Jane Doe").languages(Collections.singletonList(englishLanguageEntity)).build();
        TutorEntity entity2 =
                TutorEntity.builder().id(2L).name("John Doe").languages(Collections.singletonList(dutchLanguageEntity)).build();


        when(tutorRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        GetTutorsResponse actualResult = getTutorsUseCase.getTutors();

        Tutor tutor1 = Tutor.builder().id(1L).name("Jane Doe").languages(englishLanguageEntity.getName()).build();
        Tutor tutor2 = Tutor.builder().id(2L).name("John Doe").languages(dutchLanguageEntity.getName()).build();

        GetTutorsResponse expectedResult = GetTutorsResponse.builder().tutors(List.of(tutor1, tutor2)).build();
        assertEquals(expectedResult, actualResult);

        verify(tutorRepositoryMock).findAll();
    }
}
