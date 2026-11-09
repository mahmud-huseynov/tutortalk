package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.language.CreateLanguageUseCase;
import nl.fontys.s3.business.language.LanguageValidator;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.domain.language.CreateLanguageRequest;
import nl.fontys.s3.domain.language.CreateLanguageResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateLanguageUseCaseImpl implements CreateLanguageUseCase {
    private final LanguageRepository languageRepository;
    private final LanguageValidator languageValidator;

    @Transactional
    @Override
    public CreateLanguageResponse createLanguage(CreateLanguageRequest request) {

        try {
            languageValidator.validate(request.getName(), request.getCode());
        } catch (InvalidLanguageException e) {
            throw new InvalidLanguageException("Language already exists.");
        }

        LanguageEntity savedLanguage = saveNewLanguage(request);

        return CreateLanguageResponse.builder()
                .languageId(savedLanguage.getId())
                .build();
    }

    private LanguageEntity saveNewLanguage(CreateLanguageRequest request){
        LanguageEntity newLanguage = LanguageEntity.builder()
                .code(request.getCode())
                .name(request.getName())
                .build();

        return languageRepository.save(newLanguage);
    }

}
