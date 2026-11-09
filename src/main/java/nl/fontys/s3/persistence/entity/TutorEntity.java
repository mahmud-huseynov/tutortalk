package nl.fontys.s3.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "tutor")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "tutor_language",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<LanguageEntity> languages;
}
