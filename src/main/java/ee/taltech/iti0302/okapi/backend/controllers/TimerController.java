package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timer")
public class TimerController {
    private final Timer timer;

    public TimerController(Timer timer) {
        this.timer = timer;
    }

    //curl http://localhost:8080/timer
    @GetMapping
    public String getTimer() {
        return timer.formatTime();
    }

    //curl -X POST http://localhost:8080/timer/start
    @PostMapping("/start")
    public void startTimer() {
        timer.start(4000);
    }

    //curl -X POST http://localhost:8080/timer/stop
    @PostMapping("/stop")
    public void stopTimer() {
        timer.stop();
    }

    //curl -X POST http://localhost:8080/timer/reset
    @PostMapping("/reset")
    public void resetTimer() {
        timer.reset();
    }
}
