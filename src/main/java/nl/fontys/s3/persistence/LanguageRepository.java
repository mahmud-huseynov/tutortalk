package nl.fontys.s3.persistence;

import nl.fontys.s3.persistence.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    boolean existsByNameAndCode(String name, String code);
    LanguageEntity findById(long languageId);
    void deleteById(long countryID);

}
