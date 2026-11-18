package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import nl.fontys.s3.business.exception.InvalidTutorException;
import nl.fontys.s3.business.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.business.tutor.UpdateTutorUseCase;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.domain.tutor.UpdateTutorRequest;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.security.RoleEnum;
import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UpdateTutorUseCaseImpl implements UpdateTutorUseCase {
    private final TutorRepository tutorRepository;
    private final LanguageRepository languageRepository;
    @Setter
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void updateTutor(long id, UpdateTutorRequest request) {
        if (requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                requestAccessToken.getStudentId() != id) {
            throw new UnauthorizedDataAccessException("TUTOR_ID_NOT_FROM_LOGGED_IN_USER");
        }
        TutorEntity tutor = tutorRepository.findById(id);

        if (tutor == null) {
            throw new InvalidTutorException("Tutor not found with id: " + id);
        }

        if (isSameValues(request, tutor)) {
            throw new InvalidTutorException("Tutor values are same, no update required");
        }

        updateFields(request, tutor);
    }

    void updateFields(UpdateTutorRequest request, TutorEntity tutor) {
        tutor.setName(request.getName());
        List<Long> languageIds = request.getLanguageIds();

        List<LanguageEntity> selectedLanguages = languageRepository.findAll().stream()
                .filter(language -> languageIds.contains(language.getId()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (selectedLanguages.size() != languageIds.size()) {
            List<Long> missingLanguageIds = languageIds.stream()
                    .filter(id -> selectedLanguages.stream().noneMatch(language -> language.getId().equals(id)))
                    .collect(Collectors.toCollection(ArrayList::new));
            throw new InvalidTutorException("Language does not exist: " + missingLanguageIds);
        }

        tutor.setLanguages(new HashSet<>(selectedLanguages));
        tutorRepository.save(tutor);
    }

    boolean isSameValues(UpdateTutorRequest request, TutorEntity tutor) {
        return Objects.equals(request.getName(), tutor.getName()) &&
                Objects.equals(request.getLanguageIds(), tutor.getLanguages().stream()
                        .map(LanguageEntity::getId)
                        .collect(Collectors.toList()));
    }
}
