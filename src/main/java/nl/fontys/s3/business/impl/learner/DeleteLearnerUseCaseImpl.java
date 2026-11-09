package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidLearnerException;
import nl.fontys.s3.business.learner.DeleteLearnerUseCase;
import nl.fontys.s3.persistence.LearnerRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteLearnerUseCaseImpl implements DeleteLearnerUseCase {
    private final LearnerRepository learnerRepository;

    @Transactional
    @Override
    public void deleteLearner(long learnerId) {
        if (!learnerRepository.existsById(learnerId)) {
            throw new InvalidLearnerException("Learner not found with id: " + learnerId);
        }
        this.learnerRepository.deleteById(learnerId);
    }
}
