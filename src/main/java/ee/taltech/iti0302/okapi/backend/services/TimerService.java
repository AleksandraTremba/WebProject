package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimerService {
    @NonNull
    private TimerRepository timerRepository;

    public TimerDTO getTimerById(Long id) {
        return TimerMapper.INSTANCE.toDTO(timerRepository.findById(id).orElse(null));
    }

    public TimerDTO startTimer(Long id) {
        Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
            Timer timer = opTimer.get();
            if (timer.getRemainingTime() > 0) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime newEndTime = now.plusSeconds(timer.getRemainingTime());
                timer.setEndTime(newEndTime);
            } else {
                timer.setStartTime(LocalDateTime.now());
                timer.setEndTime(timer.getStartTime().plusSeconds(60));
            }
            timer.setRemainingTime(0);
            timerRepository.save(timer);
            return TimerMapper.INSTANCE.toDTO(timer);
        }
        return null;
    }

    public TimerDTO stopTimer(Long id) {
        Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
            Timer timer = opTimer.get();
            LocalDateTime now = LocalDateTime.now();
            long remainingTime =  ChronoUnit.SECONDS.between(now, timer.getEndTime());
            if (remainingTime > 0) {
                timer.setRemainingTime(remainingTime);
            } else {
                timer.setRemainingTime(0);
            }
            timerRepository.save(timer);
            return TimerMapper.INSTANCE.toDTO(timer);
        }
        return null;
    }

    public TimerDTO createTimer(Long customerId) {
        TimerDTO timerDTO = new TimerDTO();
        timerDTO.setCustomerId(customerId);
        timerDTO.setStartTime(LocalDateTime.now());
        timerDTO.setEndTime(timerDTO.getStartTime().plusSeconds(60));
        Timer timer = timerRepository.save(TimerMapper.INSTANCE.toEntity(timerDTO));
        timerDTO.setId(timer.getId());
        return timerDTO;
    }
}
