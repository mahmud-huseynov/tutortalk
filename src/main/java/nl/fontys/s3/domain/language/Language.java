package nl.fontys.s3.domain.language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    private Long id;
    private String name;
    private String code;
}
