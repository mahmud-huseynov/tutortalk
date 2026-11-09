package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidLanguageException;
import nl.fontys.s3.business.learner.GetLearnerUseCase;
import nl.fontys.s3.domain.learner.GetLearnerResponse;
import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.LearnerRepository;
import nl.fontys.s3.persistence.entity.LearnerEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class GetLearnerUseCaseImpl implements GetLearnerUseCase {
    private final LearnerRepository learnerRepository;

    @Transactional
    @Override
    public GetLearnerResponse getLearnerById(Long id) {
        LearnerEntity learnerEntity = learnerRepository.findById(id)
                .orElse(null);

        if (learnerEntity == null) {
            throw new InvalidLanguageException("Language not found with id: " + id);
        }

        Learner learner = Learner.builder()
                .id(learnerEntity.getId())
                .name(learnerEntity.getName())
                .language(learnerEntity.getLanguage().getName())
                .tutor(learnerEntity.getTutor().getName())
                .build();

        return GetLearnerResponse.builder()
                .learner(learner)
                .build();
    }
}
