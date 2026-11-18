package nl.fontys.s3.business.tutor;


import nl.fontys.s3.domain.tutor.UpdateTutorRequest;

public interface UpdateTutorUseCase {
    void updateTutor(long id, UpdateTutorRequest request);
}
