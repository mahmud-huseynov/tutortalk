package nl.fontys.s3.business.impl.user;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.business.user.GetUserRoleUseCase;
import nl.fontys.s3.persistence.UserRoleRepository;
import nl.fontys.s3.persistence.entity.UserRoleEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserRoleUseCaseImpl implements GetUserRoleUseCase {

    private final UserRoleRepository userRoleRepository;

    @Transactional
    @Override
    public UserRoleEntity findRoleByUserId(long userId) {
        return userRoleRepository.findByUserId(userId);
    }
}
