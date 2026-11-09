package nl.fontys.s3.business.tutor;

import nl.fontys.s3.domain.tutor.GetTutorResponse;

public interface GetTutorUseCase {
    GetTutorResponse getTutorById(Long id);
}
