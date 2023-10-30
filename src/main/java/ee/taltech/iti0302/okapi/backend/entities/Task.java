package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TaskStatus status;
}
