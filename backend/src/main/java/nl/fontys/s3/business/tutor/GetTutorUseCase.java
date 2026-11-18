package nl.fontys.s3.business.tutor;

import nl.fontys.s3.domain.tutor.GetTutorResponse;

import java.util.Optional;

public interface GetTutorUseCase {
    GetTutorResponse getTutorById(Long id);
    Optional<Long> getTutorIdByUsername(String username); // Add this line
}
