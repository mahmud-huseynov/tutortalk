package nl.fontys.s3.persistence;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class LearnerRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LearnerRepository learnerRepository;


    @Test
    void save_shouldSaveTutorWithAllFields() {
        LanguageEntity dutch = LanguageEntity.builder().name("Dutch").code("NL").build();
        List<LanguageEntity> languages = new ArrayList<>();
        languages.add(dutch);
        TutorEntity jacco = TutorEntity.builder().name("Jacco Snoeren").languages(languages).build();

        LearnerEntity mahmud = LearnerEntity.builder()
                .name("Mahmud Huseynov")
                .language(dutch)
                .tutor(jacco)
                .build();

        LearnerEntity savedLearner = learnerRepository.save(mahmud);
        assertNotNull(savedLearner.getId());

        savedLearner = entityManager.find(LearnerEntity.class, savedLearner.getId());
        LearnerEntity expectedLearner = LearnerEntity.builder()
                .id(savedLearner.getId())
                .name("Mahmud Huseynov")
                .language(dutch)
                .tutor(jacco)
                .build();
        assertEquals(expectedLearner, savedLearner);
    }
}