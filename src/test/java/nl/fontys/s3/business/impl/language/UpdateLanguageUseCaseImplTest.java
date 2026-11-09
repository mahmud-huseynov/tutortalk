package nl.fontys.s3.business.impl.language;

import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.domain.language.UpdateLanguageRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateLanguageUseCaseImplTest {

    @Mock
    private LanguageRepository languageRepositoryMock;

    @InjectMocks
    private UpdateLanguageUseCaseImpl updateLanguageUseCase;

    @Test
    void updateLanguage_languageFound_shouldUpdate() {
        LanguageEntity languageEntity = LanguageEntity.builder().id(1L).code("EN").name("English").build();

        UpdateLanguageRequest request = UpdateLanguageRequest.builder().code("NL").name("Dutch").build();

        when(languageRepositoryMock.findById(1L)).thenReturn(languageEntity);

        updateLanguageUseCase.updateLanguage(1L, request);

        verify(languageRepositoryMock).save(languageEntity);
    }

    @Test
    void updateLanguage_languageNotFound_shouldThrowException() {
        UpdateLanguageRequest request = UpdateLanguageRequest.builder().code("EN").name("English").build();

        when(languageRepositoryMock.findById(1L)).thenReturn(null);

        assertThrows(InvalidLanguageException.class, () -> updateLanguageUseCase.updateLanguage(1L, request));

        verify(languageRepositoryMock, never()).save(any());
    }
}