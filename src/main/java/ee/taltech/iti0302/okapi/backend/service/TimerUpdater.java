package ee.taltech.iti0302.okapi.backend.service;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerUpdater {

    private Timer timer;

    public TimerUpdater(Timer timer) {
        this.timer = timer;
    }

    @Scheduled(fixedRate = 1000) // Run every 1 second
    public void updateTimer() {
        timer.update();
    }
}
