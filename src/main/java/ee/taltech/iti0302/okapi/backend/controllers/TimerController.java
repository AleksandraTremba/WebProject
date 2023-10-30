package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/timer")
public class TimerController {

    @Autowired
    @NonNull
    private TimerService timerService;


    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    //curl http://localhost:8080/api/timer/id
    @GetMapping("/{id}")
    public TimerDTO getTimer(@PathVariable Long id) {
        return timerService.getTimerById(id);
    }


    //curl -X POST http://localhost:8080/api/timer/start/id
    @PostMapping("/start")
    public TimerDTO startTimer(@RequestParam Long id) {
        return timerService.startTimer(id);
    }

    //curl -X POST http://localhost:8080/api/timer/stop/id
//    @PostMapping("/stop")
//    public void stopTimer(@RequestParam Long id) {
//        timerService.stopTimer(id);
//    }

    //curl -X POST http://localhost:8080/api/timer/reset/id
//    @PostMapping("/reset")
//    public void resetTimer(@RequestParam Long id) {
//        timerService.resetTimer(id);
//    }

//    @PostMapping("/fetch")
//    public Long fetchTime(@RequestParam Long id) {
//        TimerDTO dto = timerService.getTimerById(id);
//        if (dto != null)
//            return dto.getSeconds();
//        return null;
//    }
}
