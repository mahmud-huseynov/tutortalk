package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.learner.LearnerStatisticsUseCase;
import nl.fontys.s3.persistence.LearnerRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LearnerStatisticsUseCaseImpl implements LearnerStatisticsUseCase {

    private final LearnerRepository learnerRepository;
    @Transactional
    @Override
    public long getNumberOfLearners() {
        return learnerRepository.countLearners();
    }
}
