package nl.fontys.s3.business.impl.learner;

import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.entity.LearnerEntity;

public class LearnerConverter {
    private LearnerConverter() {
    }

    public static Learner convert(LearnerEntity learner) {
        return Learner.builder()
                .id(learner.getId())
                .name(learner.getName())
                .language(learner.getLanguage() != null ? learner.getLanguage().getName() : null)
                .tutor(learner.getTutor() != null ? learner.getTutor().getName() : null)
                .build();
    }
}
