package nl.fontys.s3.business.language;

import nl.fontys.s3.domain.language.CreateLanguageRequest;
import nl.fontys.s3.domain.language.CreateLanguageResponse;

public interface CreateLanguageUseCase {

    CreateLanguageResponse createLanguage(CreateLanguageRequest request);
}
