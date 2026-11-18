package nl.fontys.s3.business.tutor;

import nl.fontys.s3.domain.tutor.CreateTutorRequest;
import nl.fontys.s3.domain.tutor.CreateTutorResponse;

public interface CreateTutorUseCase {
    CreateTutorResponse createTutor(CreateTutorRequest request);
}
