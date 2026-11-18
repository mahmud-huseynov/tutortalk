package nl.fontys.s3.domain.learner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLearnerRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long languageId;
    @NotNull
    private Long tutorId;
}
