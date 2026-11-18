package nl.fontys.s3.business.impl.auth;

import nl.fontys.s3.domain.auth.SignupRequest;
import nl.fontys.s3.domain.auth.SignupResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private LanguageRepository languageRepositoryMock;
    @InjectMocks
    private SignupUseCaseImpl signupUseCase;

    @Test
    void signupTutor_validSignupRequest_shouldReturnSignupResponse() {
        SignupRequest request = SignupRequest.builder()
                .username("validUser")
                .password("validPassword")
                .email("valid@example.com")
                .number("123456789")
                .languageIds(List.of(1L, 2L))
                .build();

        LanguageEntity language1 = LanguageEntity.builder().id(1L).name("English").build();
        LanguageEntity language2 = LanguageEntity.builder().id(2L).name("Spanish").build();

        when(languageRepositoryMock.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(language1, language2));

        when(passwordEncoderMock.encode("validPassword"))
                .thenReturn("encodedPassword");

        UserEntity savedUser = UserEntity.builder()
                .username("validUser")
                .password("encodedPassword")
                .build();

        when(userRepositoryMock.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        SignupResponse response = signupUseCase.signupTutor(request);

        assertNotNull(response);
        assertEquals("validUser", response.getUsername());
        assertEquals("Tutor signup successful", response.getMessage());
    }

    @Test
    void signupLearner_validSignupRequest_shouldReturnSignupResponse() {
        SignupRequest request = SignupRequest.builder()
                .username("validUser")
                .password("validPassword")
                .languageId(1L)
                .build();

        LanguageEntity language = LanguageEntity.builder().id(1L).name("English").build();

        when(languageRepositoryMock.findById(request.getLanguageId())).thenReturn(Optional.of(language));
        when(passwordEncoderMock.encode("validPassword")).thenReturn("encodedPassword");

        UserEntity savedUser = UserEntity.builder()
                .username("validUser")
                .password("encodedPassword")
                .build();

        when(userRepositoryMock.save(any(UserEntity.class))).thenReturn(savedUser);

        SignupResponse response = signupUseCase.signupLearner(request);

        assertNotNull(response);
        assertEquals("validUser", response.getUsername());
        assertEquals("Learner signup successful", response.getMessage());
    }

    @Test
    void signupLearner_invalidLanguageId_shouldThrowIllegalArgumentException() {
        SignupRequest request = SignupRequest.builder()
                .username("validUser")
                .password("validPassword")
                .languageId(1L)
                .build();

        // Ensure that the mock returns Optional.empty() instead of null
        when(languageRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> signupUseCase.signupLearner(request));

        // Verify that the userRepository.save method is never called
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }
}
