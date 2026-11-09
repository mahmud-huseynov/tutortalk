package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.business.language.GetLanguageUseCase;
import nl.fontys.s3.domain.language.GetLanguageResponse;
import nl.fontys.s3.domain.language.Language;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class GetLanguageUseCaseImpl implements GetLanguageUseCase {
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public GetLanguageResponse getLanguageById(Long id) {
        LanguageEntity languageEntity = languageRepository.findById(id)
                .orElse(null);

        if (languageEntity == null) {
            throw new InvalidLanguageException("Language not found with id: " + id);
        }

        Language language = LanguageConverter.convert(languageEntity);

        return GetLanguageResponse.builder()
                .language(language)
                .build();
    }
}
