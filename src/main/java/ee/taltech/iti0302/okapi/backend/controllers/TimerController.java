package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerResetDTO;
import ee.taltech.iti0302.okapi.backend.services.TimerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/timers")
public class TimerController {
    private final TimerService timerService;

    //curl http://localhost:8080/api/timer
    @GetMapping("/{id}")
    public TimerDTO getTimer(@PathVariable Long id) {
        return timerService.getTimerById(id);
    }

    //curl -X POST http://localhost:8080/api/timer/start&id?=
    @PostMapping("/start")
    public TimerDTO startTimer(@RequestBody @Valid TimerDTO dto) {
        return timerService.startTimer(dto);
    }

    //curl -X POST http://localhost:8080/api/timer/stop&id?=
    @PostMapping("/stop")
    public TimerDTO stopTimer(@RequestParam Long id) {
        return timerService.stopTimer(id);
    }

    @PostMapping("/reset")
    public TimerDTO resetTimer(@RequestBody TimerResetDTO request) {
        return timerService.resetTimer(request);
    }
}
