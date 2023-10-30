package ee.taltech.iti0302.okapi.backend.dto;

import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {
    private long id;
    private String title;
    private String description;
    private TaskStatus status;
}
