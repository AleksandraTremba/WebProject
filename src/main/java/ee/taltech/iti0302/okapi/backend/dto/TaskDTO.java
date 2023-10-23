package ee.taltech.iti0302.okapi.backend.dto;

import ee.taltech.iti0302.okapi.backend.entities.TaskStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class TaskDTO {
    @NonNull
    private long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private TaskStatus status;
}
