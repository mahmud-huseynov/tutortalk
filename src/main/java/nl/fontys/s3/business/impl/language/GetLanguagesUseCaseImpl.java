package nl.fontys.s3.business.impl.language;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.language.GetLanguagesUseCase;
import nl.fontys.s3.domain.language.GetLanguagesResponse;
import nl.fontys.s3.domain.language.Language;
import nl.fontys.s3.persistence.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetLanguagesUseCaseImpl implements GetLanguagesUseCase {
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public GetLanguagesResponse getLanguages() {
        List<Language> languages = languageRepository.findAll()
                .stream()
                .map(LanguageConverter::convert)
                .toList();

        return GetLanguagesResponse.builder()
                .languages(languages)
                .build();
    }
}
