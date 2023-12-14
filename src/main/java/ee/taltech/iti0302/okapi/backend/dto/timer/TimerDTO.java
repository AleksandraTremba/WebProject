package ee.taltech.iti0302.okapi.backend.dto.timer;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimerDTO {
    @NotBlank
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // In ms
    @NotBlank
    private Integer runningTime;
    private Long remainingTime;
}
