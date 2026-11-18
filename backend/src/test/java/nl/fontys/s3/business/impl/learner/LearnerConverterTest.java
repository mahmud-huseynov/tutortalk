package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LearnerConverterTest {

    @Test
    void convert_languageNotNull_shouldReturnLanguageName() {
        LanguageEntity languageEntity = new LanguageEntity();
        languageEntity.setName("English");

        LearnerEntity learnerEntity = new LearnerEntity();
        learnerEntity.setId(1L);
        learnerEntity.setName("John");
        learnerEntity.setLanguage(languageEntity);

        Learner learner = LearnerConverter.convert(learnerEntity);

        assertEquals("English", learner.getLanguage());
    }

    @Test
    void convert_languageNull_shouldReturnNullLanguageName() {
        LearnerEntity learnerEntity = new LearnerEntity();
        learnerEntity.setId(1L);
        learnerEntity.setName("John");

        Learner learner = LearnerConverter.convert(learnerEntity);

        assertNull(learner.getLanguage());
    }

    @Test
    void convert_tutorNotNull_shouldReturnTutorName() {
        TutorEntity tutorEntity = new TutorEntity();
        tutorEntity.setName("Alice");

        LearnerEntity learnerEntity = new LearnerEntity();
        learnerEntity.setId(1L);
        learnerEntity.setName("John");
        learnerEntity.setTutor(tutorEntity);

        Learner learner = LearnerConverter.convert(learnerEntity);

        assertEquals("Alice", learner.getTutor());
    }

    @Test
    void convert_tutorNull_shouldReturnNullTutorName() {
        LearnerEntity learnerEntity = new LearnerEntity();
        learnerEntity.setId(1L);
        learnerEntity.setName("John");

        Learner learner = LearnerConverter.convert(learnerEntity);

        assertNull(learner.getTutor());
    }
}
