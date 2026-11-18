package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TutorConverterTest {

    @Test
    void convert_withNonNullAndNonEmptyLanguages() {
        Set<LanguageEntity> languages = new HashSet<>();
        languages.add(LanguageEntity.builder().name("English").code("en").build());
        languages.add(LanguageEntity.builder().name("French").code("fr").build());
        TutorEntity tutorEntity = TutorEntity.builder()
                .id(1L)
                .name("John Doe")
                .languages(languages)
                .build();

        Tutor expectedTutor = Tutor.builder()
                .id(1L)
                .name("John Doe")
                .languages("English, French")
                .build();

        assertEquals(expectedTutor, TutorConverter.convert(tutorEntity));
    }

    @Test
    void convert_withNullLanguages() {
        TutorEntity tutorEntity = TutorEntity.builder()
                .id(1L)
                .name("Jane Smith")
                .languages(null)
                .build();

        Tutor expectedTutor = Tutor.builder()
                .id(1L)
                .name("Jane Smith")
                .languages("")
                .build();

        assertEquals(expectedTutor, TutorConverter.convert(tutorEntity));
    }

    @Test
    void convert_withEmptyLanguages() {
        TutorEntity tutorEntity = TutorEntity.builder()
                .id(1L)
                .name("Alice")
                .languages(new HashSet<>())
                .build();

        Tutor expectedTutor = Tutor.builder()
                .id(1L)
                .name("Alice")
                .languages("")
                .build();

        assertEquals(expectedTutor, TutorConverter.convert(tutorEntity));
    }

    @Test
    void convert_withNullTutorEntity() {
        assertThrows(NullPointerException.class, () -> TutorConverter.convert(null));
    }
}
