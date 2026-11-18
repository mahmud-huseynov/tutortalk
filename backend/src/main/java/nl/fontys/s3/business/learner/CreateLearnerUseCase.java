package nl.fontys.s3.business.learner;


import nl.fontys.s3.domain.learner.CreateLearnerRequest;
import nl.fontys.s3.domain.learner.CreateLearnerResponse;

public interface CreateLearnerUseCase {
    CreateLearnerResponse createLearner(CreateLearnerRequest request);
}
