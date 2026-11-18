package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.tutor.GetTutorsUseCase;
import nl.fontys.s3.domain.tutor.GetTutorsResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetTutorsUseCaseImpl implements GetTutorsUseCase {
    private final TutorRepository tutorRepository;

    @Transactional
    @Override
    public GetTutorsResponse getTutors() {
        List<Tutor> tutors = tutorRepository.findAll()
                .stream()
                .map(TutorConverter::convert)
                .toList();

        return GetTutorsResponse.builder()
                .tutors(tutors)
                .build();
    }

}
