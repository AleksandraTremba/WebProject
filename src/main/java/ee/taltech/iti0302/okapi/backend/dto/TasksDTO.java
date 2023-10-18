package ee.taltech.iti0302.okapi.backend.dto;

import ee.taltech.iti0302.okapi.backend.entities.TaskStatus;

public class TasksDTO {
    private long id;
    private String title;
    private String description;
    private TaskStatus status;

    public TasksDTO() {}

    public TasksDTO (long id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public TasksDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
