package nl.fontys.s3.business.user;

import nl.fontys.s3.persistence.entity.UserEntity;

public interface GetUserUseCase {
    UserEntity findByUsername(String username);
}
