package nl.fontys.s3.business.impl.language;

import nl.fontys.s3.business.language.LanguageValidator;
import nl.fontys.s3.domain.language.CreateLanguageRequest;
import nl.fontys.s3.domain.language.CreateLanguageResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateLanguageUseCaseImplTest {

    @Mock
    private LanguageRepository languageRepositoryMock;

    @Mock
    private LanguageValidator languageValidatorMock;

    @InjectMocks
    private CreateLanguageUseCaseImpl createLanguageUseCase;

    @Test
    void createLanguage_shouldReturnNewLanguage() {
        CreateLanguageRequest request = CreateLanguageRequest.builder().code("EN").name("English").build();

        when(languageRepositoryMock.save(any(LanguageEntity.class)))
                .thenAnswer(invocation -> {
                    LanguageEntity entityArgument = invocation.getArgument(0);
                    entityArgument.setId(1L);
                    return entityArgument;
                });

        CreateLanguageResponse response = createLanguageUseCase.createLanguage(request);

        assertEquals(1L, response.getLanguageId());

        verify(languageRepositoryMock).save(any(LanguageEntity.class));
    }
}