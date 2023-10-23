package ee.taltech.iti0302.okapi.backend.service;

import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import ee.taltech.iti0302.okapi.backend.states.TimerState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimerService {
    @NonNull
    @Autowired
    private TimerRepository timerRepository;

    public TimerDTO getTimerById(Long id) {
        return TimerMapper.INSTANCE.toDTO(timerRepository.findById(id).orElse(null));
    }

    public TimerDTO startTimer(Long id) {
        Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
            Timer timer = opTimer.get();
            timer.start(4000);
            timerRepository.save(timer); // You need to save the updated timer
            return TimerMapper.INSTANCE.toDTO(timer);
        } else {
            Timer timer = new Timer();
            timerRepository.save(timer);
            return TimerMapper.INSTANCE.toDTO(timer);
        }
    }

    public TimerDTO stopTimer(Long id) {
        Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
            Timer timer = opTimer.get();
            timer.stop();
            return TimerMapper.INSTANCE.toDTO(timer);
        }
        return null;
    }

    public TimerDTO resetTimer(Long id) {
        Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
            Timer timer = opTimer.get();
            timer.reset();
            return TimerMapper.INSTANCE.toDTO(timer);
        }
        return null;
    }
}
