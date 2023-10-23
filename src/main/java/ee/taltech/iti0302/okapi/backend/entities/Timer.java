package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.states.TimerState;
import jakarta.persistence.*;
import lombok.*;

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
    private Long seconds;
//    @NonNull
//    @Column(nullable = false)
    //private TimerState state;

    public synchronized void start(long seconds) {
//        switch (state) {
//            case PENDING -> {
//                this.seconds = seconds;
//                state = RUNNING;
//            }
//
//            case RUNNING -> {
//                Thread countdownThread = new Thread(() -> {
//                    while (state.equals(RUNNING) && this.seconds > 0) {
//                        try {
//                            Thread.sleep(1000); // 1 second
//                            this.seconds--;
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                    state = TimerState.PENDING;
//                });
//                countdownThread.start();
//            }
//
//            case PAUSED -> {
//                state = RUNNING;
//            }
//        }
    }

    public synchronized void stop() {
        //state = PAUSED;
    }

    public synchronized void reset() {
        seconds = 1L;
        //state = TimerState.PENDING;

    }

    public synchronized void update() {
        // Implement logic to decrement timer value
        //if (state.equals(RUNNING) && seconds > 0) {
            seconds--;
       // }
    }
}
