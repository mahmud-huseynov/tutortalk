package nl.fontys.s3.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 2, max = 20)
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @Length(max = 100)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id")
    private TutorEntity tutor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "learner_id")
    private LearnerEntity learner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserRoleEntity> userRoles;
}
