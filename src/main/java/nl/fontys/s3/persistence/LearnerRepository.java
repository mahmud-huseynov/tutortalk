package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.LearnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LearnerRepository extends JpaRepository<LearnerEntity, Long> {
    boolean existsByName(String name);


    LearnerEntity findById(long learnerId);


    void deleteById(long learnerId);
}
