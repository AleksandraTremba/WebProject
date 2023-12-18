package ee.taltech.iti0302.okapi.backend.dto.task;

import lombok.Data;

@Data
public class TaskDataRequestDTO {
    int page;
    long customerId;
}
