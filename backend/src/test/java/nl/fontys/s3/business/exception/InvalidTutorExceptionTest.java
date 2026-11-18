package nl.fontys.s3.business.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvalidTutorExceptionTest {
    @Test
    void throwException_Default() {
        InvalidTutorException exception = new InvalidTutorException();

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("TUTOR_INVALID", exception.getReason());
    }

    @Test
    void throwException_getErrorCode() {
        String errorCode = "CUSTOM_ERROR_CODE";
        InvalidTutorException exception = new InvalidTutorException(errorCode);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(errorCode, exception.getReason());
    }
}