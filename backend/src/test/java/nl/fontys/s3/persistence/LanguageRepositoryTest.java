package nl.fontys.s3.persistence;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class LanguageRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void save_shouldSaveLanguageWithAllFields() {
        LanguageEntity dutch = LanguageEntity.builder()
                .name("Dutch")
                .code("NL")
                .build();

        LanguageEntity savedLanguage = languageRepository.save(dutch);
        assertNotNull(savedLanguage.getId());

        savedLanguage = entityManager.find(LanguageEntity.class, savedLanguage.getId());
        LanguageEntity expectedLanguage = LanguageEntity.builder()
                .id(savedLanguage.getId())
                .name("Dutch")
                .code("NL")
                .build();
        assertEquals(expectedLanguage, savedLanguage);
    }
}