package nl.fontys.s3.persistence;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TutorRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TutorRepository tutorRepository;

    @Test
    void save_shouldSaveTutorWithAllFields() {
        LanguageEntity dutch = saveLanguage("Dutch", "NL");
        LanguageEntity english = saveLanguage("English", "EN");
        Set<LanguageEntity> languages = new HashSet<>();
        languages.add(dutch);
        languages.add(english);
        TutorEntity mahmud = TutorEntity.builder()
                .name("Mahmud Huseynov")
                .email("mahmudhuseynov@gmail.com")
                .number("0638330429")
                .languages(languages)
                .build();

        TutorEntity savedTutor = tutorRepository.save(mahmud);
        assertNotNull(savedTutor.getId());

        savedTutor = entityManager.find(TutorEntity.class, savedTutor.getId());
        TutorEntity expectedTutor = TutorEntity.builder()
                .id(savedTutor.getId())
                .name("Mahmud Huseynov")
                .email("mahmudhuseynov@gmail.com")
                .number("0638330429")
                .languages(languages)
                .build();
        assertEquals(expectedTutor, savedTutor);
    }

    private LanguageEntity saveLanguage(String name, String code) {
        LanguageEntity language = LanguageEntity.builder().name(name).code(code).build();
        entityManager.persist(language);
        return language;
    }

}