package nl.fontys.s3.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTutorException extends ResponseStatusException {
    public InvalidTutorException() {
        super(HttpStatus.BAD_REQUEST, "TUTOR_INVALID");
    }

    public InvalidTutorException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
