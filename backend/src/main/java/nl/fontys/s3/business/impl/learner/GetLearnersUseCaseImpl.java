package nl.fontys.s3.business.impl.learner;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.learner.GetLearnersUseCase;
import nl.fontys.s3.domain.learner.GetLearnersResponse;
import nl.fontys.s3.domain.learner.Learner;
import nl.fontys.s3.persistence.LearnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetLearnersUseCaseImpl implements GetLearnersUseCase {
    private final LearnerRepository learnerRepository;
    @Transactional
    @Override
    public GetLearnersResponse getLearners() {
        List<Learner> learners = learnerRepository.findAll()
                .stream()
                .map(LearnerConverter::convert)
                .toList();

        return GetLearnersResponse.builder()
                .learners(learners)
                .build();
    }
}
