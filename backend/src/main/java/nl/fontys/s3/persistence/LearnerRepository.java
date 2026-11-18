package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.LearnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LearnerRepository extends JpaRepository<LearnerEntity, Long> {
    boolean existsByName(String name);

    LearnerEntity findById(long learnerId);

    void deleteById(long learnerId);

    List<LearnerEntity> findByTutorId(long tutorId);

    @Query("SELECT COUNT(l) FROM LearnerEntity l")
    long countLearners();
}
