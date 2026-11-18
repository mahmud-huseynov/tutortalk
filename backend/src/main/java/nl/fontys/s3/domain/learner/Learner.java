package nl.fontys.s3.domain.learner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Learner {
    private Long id;

    private String name;

    private String language;

    private String tutor;
}
