package nl.fontys.s3.business.impl.auth;

import lombok.AllArgsConstructor;
import nl.fontys.s3.business.auth.SignupUseCase;
import nl.fontys.s3.domain.auth.SignupRequest;
import nl.fontys.s3.domain.auth.SignupResponse;
import nl.fontys.s3.persistence.LanguageRepository;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.*;
import nl.fontys.s3.persistence.security.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SignupUseCaseImpl implements SignupUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LanguageRepository languageRepository;

    @Override
    @Transactional
    public SignupResponse signupTutor(SignupRequest signupRequest) {
        List<LanguageEntity> languageEntities = languageRepository.findAllById(signupRequest.getLanguageIds());

        TutorEntity tutor = TutorEntity.builder()
                .name(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .number(signupRequest.getNumber())
                .languages(new HashSet<>(languageEntities))
                .build();

        UserEntity user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .tutor(tutor)
                .build();

        Set<UserRoleEntity> userRoles = new HashSet<>();
        userRoles.add(UserRoleEntity.builder().role(RoleEnum.TUTOR).user(user).build());
        user.setUserRoles(userRoles);

        user = userRepository.save(user);

        return SignupResponse.builder()
                .username(user.getUsername())
                .message("Tutor signup successful")
                .build();
    }

    @Override
    @Transactional
    public SignupResponse signupLearner(SignupRequest signupRequest) {
        LanguageEntity language = languageRepository.findById(signupRequest.getLanguageId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid language ID"));

        LearnerEntity learner = LearnerEntity.builder()
                .name(signupRequest.getUsername())
                .language(language)
                .build();

        UserEntity user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .learner(learner)
                .build();

        Set<UserRoleEntity> userRoles = new HashSet<>();
        userRoles.add(UserRoleEntity.builder().role(RoleEnum.LEARNER).user(user).build());
        user.setUserRoles(userRoles);

        user = userRepository.save(user);

        return SignupResponse.builder()
                .username(user.getUsername())
                .message("Learner signup successful")
                .build();
    }
}
