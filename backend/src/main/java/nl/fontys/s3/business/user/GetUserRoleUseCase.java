package nl.fontys.s3.business.user;

import nl.fontys.s3.persistence.entity.UserRoleEntity;

public interface GetUserRoleUseCase {
    UserRoleEntity findRoleByUserId(long userId);
}
