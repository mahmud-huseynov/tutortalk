package nl.fontys.s3.business.learner;


import nl.fontys.s3.domain.learner.GetLearnerResponse;

public interface GetLearnerUseCase {
    GetLearnerResponse getLearnerById(Long id);
}
