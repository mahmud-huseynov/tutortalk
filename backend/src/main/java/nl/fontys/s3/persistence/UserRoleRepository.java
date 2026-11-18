package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByUserId(Long userId);
}