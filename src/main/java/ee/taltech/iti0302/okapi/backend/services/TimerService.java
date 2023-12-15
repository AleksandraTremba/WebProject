package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerResetDTO;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.dto.timer.DummyTimer;
import ee.taltech.iti0302.okapi.backend.exceptions.ApplicationRuntimeException;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class TimerService {
    private final TimerRepository timerRepository;

    private DummyTimer createNullTimer() {
        return DummyTimer.builder()
                .startTime(null)
                .endTime(null)
                .remainingTime(null)
                .runningTime(0)
                .build();
    }

    public TimerDTO getTimerById(Long id) {
        return TimerMapper.INSTANCE.toDTO(timerRepository.findById(id).orElse(null));
    }

    public Long createTimer(Long customerId) {
        Timer timer = new Timer();
        timer.setCustomerId(customerId);

        timerRepository.save(timer);
        return timer.getId();
    }

    public TimerDTO startTimer(TimerDTO dto) {
        Long id = dto.getId();
        Integer time = dto.getRunningTime();

        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null)
            return null;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end;

        if (!timer.getRunningTime().equals(time))
            end = now.plusSeconds(time);
        else
            end = now.plusSeconds(timer.getRemainingTime());

        Long remainingTime = 0L;

        dto.setStartTime(now);
        dto.setEndTime(end);
        dto.setRemainingTime(remainingTime);

        TimerMapper.INSTANCE.updateTimerFromExternalDataset(dto, timer);
        timerRepository.save(timer);

        return dto;
    }

    public TimerDTO stopTimer(Long id) {
        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null)
            return null;

        LocalDateTime now = LocalDateTime.now();
        Long remainingTime = ChronoUnit.SECONDS.between(now, timer.getEndTime());

        TimerDTO dto = TimerMapper.INSTANCE.toDTO(timer);
        dto.setRemainingTime(remainingTime);

        TimerMapper.INSTANCE.updateTimerFromExternalDataset(dto, timer);
        timerRepository.save(timer);

        return dto;
    }

    public TimerDTO resetTimer(TimerResetDTO request) {
        Timer timer = timerRepository.findById(request.getId()).orElse(null);

        if (timer == null)
            throw new ApplicationRuntimeException("Timer does not exists!");

        if (!timer.getCustomerId().equals(request.getCustomerId()))
            throw new ApplicationRuntimeException("You cannot change others' timers, stop it!");

        DummyTimer dummy = createNullTimer();
        TimerMapper.INSTANCE.updateTimerFromExternalDataset(dummy, timer);
        timerRepository.save(timer);

        return TimerMapper.INSTANCE.toDTO(timer);
    }

    public void deleteTimer(Long id) {
        timerRepository.deleteById(id);
    }
}
