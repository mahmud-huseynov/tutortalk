package nl.fontys.s3.domain.learner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateLearnerRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long languageId;
    @NotNull
    private Long tutorId;
}
