package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TimerWebSocketController {
    private final Timer timer;

    public TimerWebSocketController(Timer timer) {
        this.timer = timer;
    }
    @MessageMapping("/api/timer")
    @SendTo("/api/timer/tick")
    public int timerTick() {
        return timer.getTimeRemaining();
    }
}