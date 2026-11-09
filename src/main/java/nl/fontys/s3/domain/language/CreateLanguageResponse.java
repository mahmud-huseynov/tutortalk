package nl.fontys.s3.domain.language;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLanguageResponse {
    private Long languageId;
}
