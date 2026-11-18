package nl.fontys.s3.business.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvalidLearnerExceptionTest {
    @Test
    void throwException_Default() {
        InvalidLearnerException exception = new InvalidLearnerException();

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("LEARNER_INVALID", exception.getReason());
    }

    @Test
    void throwException_getErrorCode() {
        String errorCode = "CUSTOM_ERROR_CODE";
        InvalidLearnerException exception = new InvalidLearnerException(errorCode);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(errorCode, exception.getReason());
    }

}