package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidLearnerException;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.learner.CreateLearnerUseCase;
import nl.fontys.s3.domain.learner.CreateLearnerRequest;
import nl.fontys.s3.domain.learner.CreateLearnerResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.LearnerRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CreateLearnerUseCaseImpl implements CreateLearnerUseCase {
    private final LearnerRepository learnerRepository;
    private final TutorRepository tutorRepository;
    private final LanguageRepository languageRepository;
    private final LearnerValidatorImpl learnerValidator;

    @Transactional
    @Override
    public CreateLearnerResponse createLearner(CreateLearnerRequest request) {

        try {
            learnerValidator.validate(request.getName());
        } catch (InvalidTutorException e) {
            throw new InvalidLearnerException("Learner already exists.");
        }

        LearnerEntity savedLearner = saveNewLearner(request);

        return CreateLearnerResponse.builder()
                .learnerId(savedLearner.getId())
                .build();
    }

    private LearnerEntity saveNewLearner(CreateLearnerRequest request) {
        LanguageEntity selectedLanguage = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new InvalidLearnerException("Language does not exist with ID: " + request.getLanguageId()));

        TutorEntity selectedTutor = tutorRepository.findById(request.getTutorId())
                .orElseThrow(() -> new InvalidLearnerException("Tutor does not exist with ID: " + request.getTutorId()));

        LearnerEntity newLearner = LearnerEntity.builder()
                .language(selectedLanguage)
                .tutor(selectedTutor)
                .name(request.getName())
                .build();

        return learnerRepository.save(newLearner);
    }

}
