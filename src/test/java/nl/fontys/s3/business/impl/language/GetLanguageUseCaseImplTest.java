package nl.fontys.s3.business.impl.language;

import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.domain.language.GetLanguageResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLanguageUseCaseImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private GetLanguageUseCaseImpl getLanguageUseCase;

    @Test
    void testGetLanguageById() {
        Long languageId = 1L;
        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setId(languageId);
        languageEntity.setName("English");

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(languageEntity));

        GetLanguageResponse response = getLanguageUseCase.getLanguageById(languageId);

        assertNotNull(response);
        assertNotNull(response.getLanguage());
        assertEquals(languageId, response.getLanguage().getId());
        assertEquals("English", response.getLanguage().getName());
    }

    @Test
    void testGetLanguageByIdNotFound() {
        Long languageId = 1L;

        when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

        assertThrows(InvalidLanguageException.class, () -> getLanguageUseCase.getLanguageById(languageId));
    }
}