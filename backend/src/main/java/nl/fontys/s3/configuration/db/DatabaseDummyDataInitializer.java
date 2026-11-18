package nl.fontys.s3.configuration.db;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidAdminException;
import nl.fontys.s3.business.tutor.CreateTutorUseCase;
import nl.fontys.s3.domain.tutor.CreateTutorRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.security.RoleEnum;
import nl.fontys.s3.persistence.entity.UserEntity;
import nl.fontys.s3.persistence.entity.UserRoleEntity;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseDummyDataInitializer {

    private LanguageRepository languageRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CreateTutorUseCase createTutorUseCase;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDatabaseInitialDummyData() {
        if (isDatabaseEmpty()) {
            insertSomeCountries();
            insertAdminUser();
            insertStudent();
        }
    }

    private boolean isDatabaseEmpty() {
        return languageRepository.count() == 0;
    }

    void insertAdminUser() {
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new InvalidAdminException("Admin password is not set.");
        }

        UserEntity adminUser = UserEntity.builder()
                .username("admin@fontys.nl")
                .password(passwordEncoder.encode(adminPassword))
                .build();
        UserRoleEntity adminRole = UserRoleEntity.builder().role(RoleEnum.ADMIN).user(adminUser).build();
        adminUser.setUserRoles(Set.of(adminRole));
        userRepository.save(adminUser);
    }


    void insertStudent() {
        Long languageId = 1L;
        CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
                .name("Linda")
                .languageIds(Collections.singletonList(languageId))
                .build();
        createTutorUseCase.createTutor(createTutorRequest);
    }

    void insertSomeCountries() {
        languageRepository.save(LanguageEntity.builder().code("NL").name("Netherlands").build());
        languageRepository.save(LanguageEntity.builder().code("BG").name("Bulgaria").build());
        languageRepository.save(LanguageEntity.builder().code("RO").name("Romania").build());
        languageRepository.save(LanguageEntity.builder().code("BR").name("Brazil").build());
        languageRepository.save(LanguageEntity.builder().code("CN").name("China").build());
    }
}
