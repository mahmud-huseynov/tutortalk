package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TutorRepository extends JpaRepository<TutorEntity, Long> {
    boolean existsByName(String name);

    TutorEntity findById(long tutorId);

    void deleteById(long tutorId);
    Optional<TutorEntity> findByName(String name);
}
