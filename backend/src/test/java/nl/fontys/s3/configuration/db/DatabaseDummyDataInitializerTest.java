package nl.fontys.s3.configuration.db;

import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SystemStubsExtension.class)
class DatabaseDummyDataInitializerTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private DatabaseDummyDataInitializer initializer;

    @BeforeEach
    void setUp(EnvironmentVariables environmentVariables) {
        MockitoAnnotations.openMocks(this);
        // Set the ADMIN_PASSWORD environment variable for the test
        environmentVariables.set("ADMIN_PASSWORD", "adminTestPassword");
    }

    @Test
    void testPopulateDatabaseInitialDummyDataWhenDatabaseIsEmpty() {
        when(languageRepository.count()).thenReturn(0L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        initializer.populateDatabaseInitialDummyData();

        verify(languageRepository, times(1)).count();
        verify(languageRepository, times(5)).save(any(LanguageEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(tutorRepository, times(1)).save(any(TutorEntity.class));
    }

    @Test
    void testPopulateDatabaseInitialDummyDataWhenDatabaseIsNotEmpty() {
        when(languageRepository.count()).thenReturn(1L);

        initializer.populateDatabaseInitialDummyData();

        verify(languageRepository, times(1)).count();
        verify(languageRepository, never()).save(any(LanguageEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(tutorRepository, never()).save(any(TutorEntity.class));
    }

    @Test
    void testInsertAdminUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        initializer.insertAdminUser();

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testInsertStudent() {
        initializer.insertStudent();

        verify(tutorRepository, times(1)).save(any(TutorEntity.class));
    }

    @Test
    void testInsertSomeCountries() {
        initializer.insertSomeCountries();

        verify(languageRepository, times(5)).save(any(LanguageEntity.class));
    }
}
