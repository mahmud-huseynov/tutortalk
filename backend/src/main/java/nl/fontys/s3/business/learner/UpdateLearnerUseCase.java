package nl.fontys.s3.business.learner;


import nl.fontys.s3.domain.learner.UpdateLearnerRequest;

public interface UpdateLearnerUseCase {
    void updateLearner(long id, UpdateLearnerRequest request);

}
