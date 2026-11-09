package nl.fontys.s3.domain.learner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetLearnerResponse {
    private Learner learner;
}
