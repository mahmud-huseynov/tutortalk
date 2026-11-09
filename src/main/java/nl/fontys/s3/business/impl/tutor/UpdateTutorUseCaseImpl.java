package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.tutor.UpdateTutorUseCase;
import nl.fontys.s3.domain.tutor.UpdateTutorRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UpdateTutorUseCaseImpl implements UpdateTutorUseCase {
    private final TutorRepository tutorRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public void updateTutor(long id, UpdateTutorRequest request) {
        TutorEntity tutor = tutorRepository.findById(id);

        if (tutor == null) {
            throw new InvalidTutorException("Tutor not found with id: " + id);
        }

        if (isSameValues(request, tutor)) {
            throw new InvalidTutorException("Tutor values are same, no update required");
        }

        updateFields(request, tutor);
    }

    private void updateFields(UpdateTutorRequest request, TutorEntity tutor) {
        tutor.setName(request.getName());
        List<Long> languageIds = request.getLanguageIds();

        List<LanguageEntity> selectedLanguages = languageRepository.findAll().stream()
                .filter(language -> languageIds.contains(language.getId()))
                .collect(Collectors.toList());

            if (selectedLanguages.size() != languageIds.size()) {
                List<Long> missingLanguageIds = languageIds.stream()
                        .filter(id -> selectedLanguages.stream().noneMatch(language -> language.getId().equals(id)))
                        .toList();
                throw new InvalidTutorException("Language does not exist: " + missingLanguageIds);
            }

            tutor.setLanguages(selectedLanguages);
        tutorRepository.save(tutor);
    }

    private boolean isSameValues(UpdateTutorRequest request, TutorEntity tutor) {
        return request.getName() != null && request.getName().equals(tutor.getName()) &&
                request.getLanguageIds() != null && request.getLanguageIds().equals(
                tutor.getLanguages().stream()
                        .map(LanguageEntity::getId)
                        .collect(Collectors.toList())
        );
    }
}
