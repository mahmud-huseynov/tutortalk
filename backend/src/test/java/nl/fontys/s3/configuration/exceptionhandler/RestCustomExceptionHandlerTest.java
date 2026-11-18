package nl.fontys.s3.configuration.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RestCustomExceptionHandlerTest {

    @InjectMocks
    private RestCustomExceptionHandler exceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private WebRequest webRequest;

    @Mock
    private ConstraintViolationException constraintViolationException;

    @Mock
    private ResponseStatusException responseStatusException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
    }

    @Test
    void handleMethodArgumentNotValid() {
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> responseEntity = exceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertInstanceOf(ProblemDetail.class, responseEntity.getBody());
        ProblemDetail problemDetail = (ProblemDetail) responseEntity.getBody();
        assertEquals("Invalid request", problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties());
    }

    @Test
    void handleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("field");
        when(violation.getMessage()).thenReturn("error message");
        when(constraintViolationException.getConstraintViolations()).thenReturn(Set.of(violation));

        ProblemDetail problemDetail = exceptionHandler.handleConstraintViolationException(constraintViolationException);

        assertNotNull(problemDetail);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Invalid request", problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties());
    }

    @Test
    void handleResponseStatusException() {
        when(responseStatusException.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(responseStatusException.getReason()).thenReturn("Not Found");

        ProblemDetail problemDetail = exceptionHandler.handleResponseStatusException(responseStatusException);

        assertNotNull(problemDetail);
        assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals("Invalid request", problemDetail.getDetail());
        assertNotNull(problemDetail.getProperties());
    }

}
