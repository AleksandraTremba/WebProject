package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "timers")
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @NonNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Setter
    @NonNull
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Setter
    private long remainingTime = 0;

    @Setter
    @NonNull
    @Column(nullable = false)
    private Long customerId;
}
