package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @NonNull
    private String title;

    @Setter
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Setter
    @NonNull
    private Long customerId;
}
