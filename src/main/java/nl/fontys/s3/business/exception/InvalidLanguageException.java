package nl.fontys.s3.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidLanguageException extends ResponseStatusException {
    public InvalidLanguageException() {
        super(HttpStatus.BAD_REQUEST, "LANGUAGE_INVALID");
    }

    public InvalidLanguageException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
