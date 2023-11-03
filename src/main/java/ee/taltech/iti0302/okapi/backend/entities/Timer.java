package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "timers")
public class Timer {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @NonNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Getter
    @Setter
    @NonNull
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Getter
    @Setter
    private long remainingTime = 0;

}
