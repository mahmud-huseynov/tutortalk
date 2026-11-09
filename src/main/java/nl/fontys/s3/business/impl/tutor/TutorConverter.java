package nl.fontys.s3.business.impl.tutor;

import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.entity.TutorEntity;
import nl.fontys.s3.persistence.entity.LanguageEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TutorConverter {
    private TutorConverter() {
    }

    public static Tutor convert(TutorEntity tutor) {
        List<LanguageEntity> languages = tutor.getLanguages();
        String language = "";

        if (languages != null && !languages.isEmpty()) {
            language = languages.stream()
                    .map(LanguageEntity::getName) // Assuming getName() retrieves the language name
                    .collect(Collectors.joining(", "));
        }

        return Tutor.builder()
                .id(tutor.getId())
                .name(tutor.getName())
                .languages(language)
                .build();
    }
}
