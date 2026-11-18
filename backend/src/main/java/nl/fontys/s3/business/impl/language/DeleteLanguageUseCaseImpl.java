package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.business.language.DeleteLanguageUseCase;
import nl.fontys.s3.persistence.LanguageRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteLanguageUseCaseImpl implements DeleteLanguageUseCase {
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public void deleteLanguage(long languageId) {
        if (!languageRepository.existsById(languageId)) {
            throw new InvalidLanguageException("Language not found with id: " + languageId);
        }
        languageRepository.deleteById(languageId);    }
}
