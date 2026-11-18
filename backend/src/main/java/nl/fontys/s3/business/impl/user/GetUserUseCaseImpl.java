package nl.fontys.s3.business.impl.user;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.user.GetUserUseCase;
import nl.fontys.s3.persistence.UserRepository;
import nl.fontys.s3.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
