package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimerService {
    private final TimerRepository timerRepository;

    private TimerDTO createNullDTO() {
        TimerDTO dto = new TimerDTO();
        dto.setRunningTime(0);
        dto.setRemainingTime(null);
        dto.setEndTime(null);
        dto.setStartTime(null);

        return dto;
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

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
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

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
        timerRepository.save(timer);

        return dto;
    }

    public TimerDTO resetTimer(Long id) {
        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null)
            return null;

        TimerDTO dto = createNullDTO();
        dto.setId(timer.getId());

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
        timerRepository.save(timer);

        return dto;
    }
}
