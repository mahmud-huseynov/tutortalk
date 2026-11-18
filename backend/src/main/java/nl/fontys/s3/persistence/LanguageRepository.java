package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    boolean existsByNameAndCode(String name, String code);
    Optional<LanguageEntity> findById(long languageId);
    void deleteById(long countryID);
}
