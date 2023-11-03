package ee.taltech.iti0302.okapi.backend.dto;

import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import lombok.*;

@Data
public class TaskDTO {
    private Long id;
    private Long customerId;
    private String title;
    private String description;
    private TaskStatus status;
}
