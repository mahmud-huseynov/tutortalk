package nl.fontys.s3.business.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvalidLanguageExceptionTest {
    @Test
    void throwException_Default() {
        InvalidLanguageException exception = new InvalidLanguageException();

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("LANGUAGE_INVALID", exception.getReason());
    }

    @Test
    void throwException_getErrorCode() {
        String errorCode = "CUSTOM_ERROR_CODE";
        InvalidLanguageException exception = new InvalidLanguageException(errorCode);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(errorCode, exception.getReason());
    }

}