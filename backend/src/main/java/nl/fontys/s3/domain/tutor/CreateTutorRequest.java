package nl.fontys.s3.domain.tutor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutorRequest {
    @NotBlank
    private String name;
    @NotNull
    private List<Long> languageIds;
}
