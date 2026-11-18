package nl.fontys.s3.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidLearnerException extends ResponseStatusException {

    public InvalidLearnerException() {
        super(HttpStatus.BAD_REQUEST, "LEARNER_INVALID");
    }
    public InvalidLearnerException (String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
