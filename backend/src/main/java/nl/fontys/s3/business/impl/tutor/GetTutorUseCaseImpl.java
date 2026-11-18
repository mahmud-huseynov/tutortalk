package nl.fontys.s3.business.impl.tutor;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import nl.fontys.s3.business.exception.UnauthorizedDataAccessException;
import nl.fontys.s3.business.tutor.GetTutorUseCase;
import nl.fontys.s3.configuration.security.token.AccessToken;
import nl.fontys.s3.domain.tutor.GetTutorResponse;
import nl.fontys.s3.domain.tutor.Tutor;
import nl.fontys.s3.persistence.TutorRepository;
import nl.fontys.s3.persistence.entity.LanguageEntity;
import nl.fontys.s3.persistence.entity.TutorEntity;
import nl.fontys.s3.persistence.security.RoleEnum;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetTutorUseCaseImpl implements GetTutorUseCase {
    private final TutorRepository tutorRepository;
    @Setter
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public GetTutorResponse getTutorById(Long id) {
        if (requestAccessToken == null) {
            throw new IllegalArgumentException("Access token cannot be null");
        }
        if (requestAccessToken.hasRole(RoleEnum.ADMIN.name())) {
            throw new UnauthorizedDataAccessException("TUTOR_ID_NOT_FROM_LOGGED_IN_USER");
        }
        TutorEntity tutorEntity = tutorRepository.findById(id).orElse(null);

        if (tutorEntity == null) {
            return null;
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
                .email(tutorEntity.getEmail())
                .number(tutorEntity.getNumber())
                .build();

        return GetTutorResponse.builder()
                .tutor(tutor)
                .build();
    }

    @Override
    public Optional<Long> getTutorIdByUsername(String username) {
        return tutorRepository.findByName(username).map(TutorEntity::getId);
    }
}