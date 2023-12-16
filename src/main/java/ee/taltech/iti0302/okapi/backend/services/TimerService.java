package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public TimerDTO getTimerById(Long id) {
        TimerDTO timerDTO = TimerMapper.INSTANCE.toDTO(timerRepository.findById(id).orElse(null));
        log.debug(getCurrentTime() + ": " + "Retrieved timer: {}", timerDTO);
        return timerDTO;
    }

    public Long createTimer(Long customerId) {
        Timer timer = new Timer();
        timer.setCustomerId(customerId);

        timerRepository.save(timer);
        log.debug(getCurrentTime() + ": " + "Timer created successfully. Timer ID: {}", timer.getId());
        return timer.getId();
    }

    public TimerDTO startTimer(TimerDTO dto) {
        Long id = dto.getId();
        Integer time = dto.getRunningTime();

        log.info(getCurrentTime() + ": " + "Starting timer with ID: {}", id);
        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null) {
            log.warn(getCurrentTime() + ": " + "Timer not found with ID: {}", id);
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end;

        if (!timer.getRunningTime().equals(time)) {
            end = now.plusSeconds(time);
        } else {
            end = now.plusSeconds(timer.getRemainingTime());
        }

        Long remainingTime = 0L;

        dto.setStartTime(now);
        dto.setEndTime(end);
        dto.setRemainingTime(remainingTime);

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
        timerRepository.save(timer);

        log.info(getCurrentTime() + ": " + "Timer started successfully. Timer ID: {}", id);
        return dto;
    }

    public TimerDTO stopTimer(Long id) {
        log.info(getCurrentTime() + ": " + "Stopping timer with ID: {}", id);
        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null) {
            log.warn(getCurrentTime() + ": " + "Timer not found with ID: {}", id);
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        Long remainingTime = ChronoUnit.SECONDS.between(now, timer.getEndTime());

        TimerDTO dto = TimerMapper.INSTANCE.toDTO(timer);
        dto.setRemainingTime(remainingTime);

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
        timerRepository.save(timer);

        log.info(getCurrentTime() + ": " + "Timer stopped successfully. Timer ID: {}", id);
        return dto;
    }

    public TimerDTO resetTimer(Long id) {
        log.info(getCurrentTime() + ": " + "Resetting timer with ID: {}", id);
        Timer timer = timerRepository.findById(id).orElse(null);
        if (timer == null) {
            log.warn(getCurrentTime() + ": " + "Timer not found with ID: {}", id);
            return null;
        }

        TimerDTO dto = createNullDTO();
        dto.setId(timer.getId());

        TimerMapper.INSTANCE.updateTimerFromDTO(dto, timer);
        timerRepository.save(timer);

        log.info(getCurrentTime() + ": " + "Timer reset successfully. Timer ID: {}", id);
        return dto;
    }
}
