package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.tutor.TutorValidator;
import nl.fontys.s3.persistence.TutorRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TutorValidatorImpl implements TutorValidator {
    private final TutorRepository tutorRepository;
    @Transactional
    @Override
    public void validate(String name) {
        if (tutorRepository.existsByName(name)) {
            throw new InvalidTutorException(    );
        }
    }
}
