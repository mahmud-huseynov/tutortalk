package nl.fontys.s3.business.language;

import nl.fontys.s3.business.exception.InvalidLanguageException;

public interface LanguageIdValidator {
    void validateId(Long countryId) throws InvalidLanguageException;
}
