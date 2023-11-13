package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @NonNull
    @Column(nullable = false)
    private String title;

    @Setter
    private String description;

    @Setter
    @NonNull
    @Column(nullable = false)
    private String status;

    @Setter
    @NonNull
    @Column(nullable = false)
    private Long customerId;
}
