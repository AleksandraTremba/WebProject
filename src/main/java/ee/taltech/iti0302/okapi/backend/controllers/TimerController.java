package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/timer")
public class TimerController {
    @NonNull
    private final TimerService timerService;

    //curl http://localhost:8080/api/timer
    @GetMapping("/{id}")
    public TimerDTO getTimer(@PathVariable Long id) {
        return timerService.getTimerById(id);
    }


    //curl -X POST http://localhost:8080/api/timer/start&id?=
    @PostMapping("/start")
    public TimerDTO startTimer(@RequestParam Long id) {
        return timerService.startTimer(id);
    }

    //curl -X POST http://localhost:8080/api/timer/stop&id?=
    @PostMapping("/stop")
    public TimerDTO stopTimer(@RequestParam Long id) {
        return timerService.stopTimer(id);
    }

    @PostMapping("/create")
    public TimerDTO createTimer() {
        return timerService.createTimer();
    }
}
