package nl.fontys.s3.business.impl.language;

import nl.fontys.s3.domain.language.GetLanguagesResponse;
import nl.fontys.s3.domain.language.Language;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLanguagesUseCaseImplTest {

    @Mock
    private LanguageRepository languageRepositoryMock;
    @InjectMocks
    private GetLanguagesUseCaseImpl getLanguagesUseCase;

    @Test
    void getLanguages_shouldReturnAllCountriesConverted() {

        LanguageEntity netherlandsEntity =
                LanguageEntity.builder().id(1L).code("NL").build();
        LanguageEntity bulgariaEntity =
                LanguageEntity.builder().id(2L).code("BU").build();
        when(languageRepositoryMock.findAll()).thenReturn(List.of(netherlandsEntity, bulgariaEntity));

        GetLanguagesResponse actualResult = getLanguagesUseCase.getLanguages();

        Language netherlands = Language.builder().id(1L).code("NL").build();
        Language bulgaria = Language.builder().id(2L).code("BU").build();
        GetLanguagesResponse expectedResult = GetLanguagesResponse.builder().languages(List.of(netherlands, bulgaria)).build();
        assertEquals(expectedResult, actualResult);

        verify(languageRepositoryMock).findAll();
    }
}