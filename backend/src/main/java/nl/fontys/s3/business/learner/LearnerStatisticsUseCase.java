package nl.fontys.s3.business.learner;

import org.springframework.stereotype.Service;

@Service
public interface LearnerStatisticsUseCase {

    public long getNumberOfLearners();
}

