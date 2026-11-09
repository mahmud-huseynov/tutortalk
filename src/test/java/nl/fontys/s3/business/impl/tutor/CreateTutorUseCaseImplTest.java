package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.domain.tutor.CreateTutorRequest;
import nl.fontys.s3.domain.tutor.CreateTutorResponse;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTutorUseCaseImplTest {

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private TutorValidatorImpl tutorValidator;

    @InjectMocks
    private CreateTutorUseCaseImpl createTutorUseCase;

    @Test
    void testCreateTutor() {
        CreateTutorRequest request = new CreateTutorRequest();
        request.setName("John Doe");
        List<Long> languageIds = new ArrayList<>();
        languageIds.add(1L);
        request.setLanguageIds(languageIds);

        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(1L);
        List<LanguageEntity> languageEntities = new ArrayList<>();
        languageEntities.add(languageEntity);

        when(languageRepository.findAll()).thenReturn(languageEntities);

        TutorEntity savedTutor = new TutorEntity();
        savedTutor.setId(1L);
        when(tutorRepository.save(any(TutorEntity.class))).thenReturn(savedTutor);

        CreateTutorResponse response = createTutorUseCase.createTutor(request);

        assertNotNull(response);
        assertNotNull(response.getTutorId());
        assertEquals(1L, response.getTutorId());
    }

    @Test
    void testCreateTutorWithInvalidLanguage() {
        CreateTutorRequest request = new CreateTutorRequest();
        request.setName("John Doe");
        List<Long> languageIds = new ArrayList<>();
        languageIds.add(1L);
        request.setLanguageIds(languageIds);

        when(languageRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(InvalidTutorException.class, () -> createTutorUseCase.createTutor(request));
    }
}
