package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.language.LanguageValidator;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.persistence.LanguageRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LanguageValidatorImpl implements LanguageValidator {
    private final LanguageRepository languageRepository;
    @Transactional
    @Override
    public void validate(String name, String code) {
        if (languageRepository.existsByNameAndCode(name, code)) {
            throw new InvalidLanguageException();
        }
    }
}
