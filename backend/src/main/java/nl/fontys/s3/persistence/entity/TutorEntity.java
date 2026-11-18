package nl.fontys.s3.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

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

    @NotBlank
    @Length(min = 2, max = 30)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Length(min = 2, max = 10)
    @Column(name = "number")
    private String number;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "tutor_language",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages;
}
