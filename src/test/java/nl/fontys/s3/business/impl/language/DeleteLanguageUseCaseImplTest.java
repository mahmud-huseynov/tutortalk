    package nl.fontys.s3.business.impl.language;

    import nl.fontys.s3.business.exception.InvalidLanguageException;
    import nl.fontys.s3.persistence.LanguageRepository;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;

    import static org.junit.jupiter.api.Assertions.assertThrows;
    import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    class DeleteLanguageUseCaseImplTest {

        @Mock
        private LanguageRepository languageRepositoryMock;
        @InjectMocks
        private DeleteLanguageUseCaseImpl deleteLanguageUseCase;

        @Test
        void deleteLanguage_languageFound_shouldDelete() {
            when(languageRepositoryMock.existsById(1L)).thenReturn(true);

            doNothing().when(languageRepositoryMock).deleteById(anyLong());

            deleteLanguageUseCase.deleteLanguage(1L);

            verify(languageRepositoryMock).deleteById(1L);
        }

        @Test
        void deleteLanguage_languageNotFound_shouldThrowException() {
            assertThrows(InvalidLanguageException.class, () -> deleteLanguageUseCase.deleteLanguage(1L));
        }
    }