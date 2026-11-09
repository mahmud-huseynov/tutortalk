package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.tutor.GetTutorUseCase;
import nl.fontys.s3.domain.tutor.GetTutorResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class GetTutorUseCaseImpl implements GetTutorUseCase {
    private final TutorRepository tutorRepository;

    @Transactional
    @Override
    public GetTutorResponse getTutorById(Long id) {
        TutorEntity tutorEntity = tutorRepository.findById(id).orElse(null);

        if (tutorEntity == null) {
            throw new InvalidTutorException("Tutor not found with id: " + id);
        }

        StringBuilder language = new StringBuilder();

        if (tutorEntity.getLanguages() != null) {
            for (LanguageEntity languageEntity : tutorEntity.getLanguages()) {
                language.append(languageEntity.getName()).append(", ");
            }
            language = new StringBuilder(language.toString().replaceAll(", $", ""));
        }

        Tutor tutor = Tutor.builder()
                .id(tutorEntity.getId())
                .name(tutorEntity.getName())
                .languages(language.toString())
                .build();

        return GetTutorResponse.builder()
                .tutor(tutor)
                .build();
    }
}
