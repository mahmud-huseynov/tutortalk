package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.language.UpdateLanguageUseCase;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.domain.language.UpdateLanguageRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateLanguageUseCaseImpl implements UpdateLanguageUseCase {

    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public void updateLanguage(long id, UpdateLanguageRequest request) {
        LanguageEntity language = languageRepository.findById(id);

        if (language == null) {
            throw new InvalidLanguageException("Language not found with id: " + id);
        }

        if (isSameValues(request, language)) {
            throw new InvalidLanguageException("Language values are same, no update required");
        }

        updateFields(request, language);
    }

    private void updateFields(UpdateLanguageRequest request, LanguageEntity language) {
        if (request.getName() != null) {
            language.setName(request.getName());
        }
        if (request.getCode() != null) {
            language.setCode(request.getCode());
        }

        languageRepository.save(language);
    }

    private boolean isSameValues(UpdateLanguageRequest request, LanguageEntity language) {
        return request.getName() != null && request.getName().equals(language.getName()) &&
                request.getCode() != null && request.getCode().equals(language.getCode());
    }
}
