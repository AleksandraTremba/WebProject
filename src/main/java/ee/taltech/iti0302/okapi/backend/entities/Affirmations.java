package ee.taltech.iti0302.okapi.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "affirmations")
public class Affirmations {
    @Id
    private Long id = 1L;

    @Setter
    @Column(nullable = false)
    private String affirmation;
}
