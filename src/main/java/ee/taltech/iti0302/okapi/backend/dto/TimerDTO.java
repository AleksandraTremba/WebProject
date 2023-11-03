package ee.taltech.iti0302.okapi.backend.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimerDTO {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
