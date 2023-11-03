package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

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
    @Enumerated(STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Setter
    @NonNull
    @Column(nullable = false)
    private Long customerId;
}
