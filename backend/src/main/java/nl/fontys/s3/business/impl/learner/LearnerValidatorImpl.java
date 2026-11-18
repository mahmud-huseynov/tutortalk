package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.learner.LearnerValidator;
import nl.fontys.s3.persistence.LearnerRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LearnerValidatorImpl implements LearnerValidator {
    private final LearnerRepository learnerRepository;
    @Transactional
    @Override
    public void validate(String name) {
        if (learnerRepository.existsByName(name)) {
            throw new InvalidTutorException();
        }
    }
}
