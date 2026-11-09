package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.tutor.CreateTutorUseCase;
import nl.fontys.s3.domain.tutor.CreateTutorRequest;
import nl.fontys.s3.domain.tutor.CreateTutorResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CreateTutorUseCaseImpl implements CreateTutorUseCase {
    private final TutorRepository tutorRepository;
    private final LanguageRepository languageRepository;
    private final TutorValidatorImpl tutorValidator;
    @Transactional
    @Override
    public CreateTutorResponse createTutor(CreateTutorRequest request) {

        try {
            tutorValidator.validate(request.getName());
        } catch (InvalidTutorException e) {
            throw new InvalidTutorException("Tutor already exists." );
        }

        TutorEntity savedTutor = saveNewTutor(request);

        return CreateTutorResponse.builder()
                .tutorId(savedTutor.getId())
                .build();
    }

    private TutorEntity saveNewTutor(CreateTutorRequest request) {
        List<LanguageEntity> languageEntities = languageRepository.findAll();
        List<Long> languageIds = request.getLanguageIds();

        List<LanguageEntity> selectedLanguages = languageEntities.stream()
                .filter(language -> languageIds.contains(language.getId()))
                .collect(Collectors.toList());

        if (selectedLanguages.size() != languageIds.size()) {
            List<Long> missingLanguageIds = languageIds.stream()
                    .filter(id -> selectedLanguages.stream().noneMatch(language -> language.getId().equals(id)))
                    .toList();
            throw new InvalidTutorException("Language does not exist: " + missingLanguageIds);
        }

        TutorEntity newTutor = TutorEntity.builder()
                .languages(selectedLanguages)
                .name(request.getName())
                .build();

        return tutorRepository.save(newTutor);
    }


}
