package nl.fontys.s3.business.language;

import nl.fontys.s3.domain.language.GetLanguageResponse;

public interface GetLanguageUseCase {
    GetLanguageResponse getLanguageById(Long id);
}
