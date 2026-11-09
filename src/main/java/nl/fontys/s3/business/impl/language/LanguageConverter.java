package nl.fontys.s3.business.impl.language;

import nl.fontys.s3.domain.language.Language;
import nl.fontys.s3.persistence.entity.LanguageEntity;

public class LanguageConverter {
    private LanguageConverter() {
    }

    public static Language convert(LanguageEntity languageEntity) {
        return Language.builder()
                .id(languageEntity.getId())
                .code(languageEntity.getCode())
                .name(languageEntity.getName())
                .build();
    }
}
