package nl.fontys.s3.domain.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLanguageRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
}
