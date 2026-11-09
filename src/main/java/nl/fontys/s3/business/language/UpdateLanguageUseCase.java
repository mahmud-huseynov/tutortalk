package nl.fontys.s3.business.language;

import nl.fontys.s3.domain.language.UpdateLanguageRequest;

public interface UpdateLanguageUseCase {
    void updateLanguage(long id, UpdateLanguageRequest request);
}
