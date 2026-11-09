package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.TutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TutorRepository extends JpaRepository<TutorEntity, Long> {
    boolean existsByName(String name);

    TutorEntity findById(long tutorId);

    void deleteById(long tutorId);
}
