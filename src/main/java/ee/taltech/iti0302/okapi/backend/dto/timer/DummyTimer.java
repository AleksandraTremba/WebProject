package ee.taltech.iti0302.okapi.backend.dto.timer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DummyTimer {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // In ms
    private Integer runningTime;
    private Long remainingTime;
}
