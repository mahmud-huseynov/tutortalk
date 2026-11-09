package nl.fontys.s3.domain.tutor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTutorResponse {
    private Tutor tutor;
}
