    package nl.fontys.s3.business.impl.learner;

    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;
    import nl.fontys.s3.business.exception.InvalidLearnerException;
    import nl.fontys.s3.business.exception.InvalidTutorException;
    import nl.fontys.s3.business.learner.UpdateLearnerUseCase;
    import nl.fontys.s3.domain.learner.UpdateLearnerRequest;
    import nl.fontys.s3.persistence.LanguageRepository;
    import nl.fontys.s3.persistence.LearnerRepository;
    import nl.fontys.s3.persistence.TutorRepository;
    import nl.fontys.s3.persistence.entity.LanguageEntity;
    import nl.fontys.s3.persistence.entity.LearnerEntity;
    import nl.fontys.s3.persistence.entity.TutorEntity;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    @Service
    @AllArgsConstructor
    public class UpdateLearnerUseCaseImpl implements UpdateLearnerUseCase {
        private final LearnerRepository learnerRepository;
        private final TutorRepository tutorRepository;
        private final LanguageRepository languageRepository;

        @Transactional
        @Override
        public void updateLearner(long id, UpdateLearnerRequest request) {
            LearnerEntity learner = learnerRepository.findById(id);

            if (learner == null) {
                throw new InvalidLearnerException("Learner not found with id: " + id);
            }

            if (isSameValues(request, learner)) {
                throw new InvalidLearnerException("Learner values are same, no update required");
            }

            updateFields(request, learner);
        }

        private void updateFields(UpdateLearnerRequest request, LearnerEntity learner) {
            learner.setName(request.getName());

            Long languageId = request.getLanguageId();
            Optional<LanguageEntity> languageEntityOptional = languageRepository.findById(languageId);

            if (languageEntityOptional.isEmpty()) {
                throw new InvalidLearnerException("Language not found with id: " + languageId);
            }

            LanguageEntity languageEntity = languageEntityOptional.get();
            learner.setLanguage(languageEntity);

            Long tutorId = request.getTutorId();
            Optional<TutorEntity> tutorEntityOptional = tutorRepository.findById(tutorId);

            if (tutorEntityOptional.isEmpty()) {
                throw new InvalidTutorException("Tutor not found with id: " + tutorId);
            }

            TutorEntity tutorEntity = tutorEntityOptional.get();
            learner.setTutor(tutorEntity);

            learnerRepository.save(learner);
        }


        private boolean isSameValues(UpdateLearnerRequest request, LearnerEntity learner) {
            return request.getName() != null && request.getName().equals(learner.getName()) &&
                    request.getLanguageId() != null && request.getLanguageId().equals(learner.getLanguage().getId()) &&
                    request.getTutorId() != null && request.getTutorId().equals(learner.getTutor().getId());
        }

    }
