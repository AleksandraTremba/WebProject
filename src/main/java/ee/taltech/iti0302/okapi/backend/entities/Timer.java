package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.states.TimerState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static ee.taltech.iti0302.okapi.backend.states.TimerState.*;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "timers")
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    @Column(nullable = false)
    private LocalDateTime startTime;
    @NonNull
    @Column(nullable = false)
    private LocalDateTime endTime;

}
