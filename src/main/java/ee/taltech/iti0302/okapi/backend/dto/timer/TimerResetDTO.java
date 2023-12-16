package ee.taltech.iti0302.okapi.backend.dto.timer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimerResetDTO {
    private Long id;
    private Long customerId;
}
