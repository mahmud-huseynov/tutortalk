package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.domain.tutor.GetTutorResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.TutorEntity;
import nl.fontys.s3.persistence.entity.LanguageEntity; // Import LanguageEntity
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTutorUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepositoryMock;

    @InjectMocks
    private GetTutorUseCaseImpl getTutorUseCase;

    @Test
    void getTutorById_shouldReturnTutorConverted() {
        LanguageEntity englishLanguageEntity = LanguageEntity.builder().id(1L).code("EN").name("English").build();
        Long id = 1L;

        TutorEntity tutorEntity = TutorEntity.builder()
                .id(id)
                .name("John Doe")
                .languages(Collections.singletonList(englishLanguageEntity))
                .build();

        when(tutorRepositoryMock.findById(id)).thenReturn(Optional.of(tutorEntity));

        Tutor expectedTutor = Tutor.builder()
                .id(id)
                .name("John Doe")
                .languages(englishLanguageEntity.getName()) // Convert LanguageEntity to Language
                .build();
        GetTutorResponse expectedResponse = GetTutorResponse.builder()
                .tutor(expectedTutor)
                .build();

        GetTutorResponse actualResponse = getTutorUseCase.getTutorById(id);

        assertEquals(expectedResponse, actualResponse);

        verify(tutorRepositoryMock).findById(id);
    }
}
