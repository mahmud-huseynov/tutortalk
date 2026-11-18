package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.tutor.DeleteTutorUseCase;
import nl.fontys.s3.persistence.TutorRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteTutorUseCaseImpl implements DeleteTutorUseCase {
    private final TutorRepository tutorRepository;

    @Transactional
    @Override
    public void deleteTutor(long tutorId) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new InvalidTutorException("Tutor not found with id: " + tutorId);
        }
        this.tutorRepository.deleteById(tutorId);
    }
}
