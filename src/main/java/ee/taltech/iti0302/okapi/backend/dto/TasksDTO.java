package ee.taltech.iti0302.okapi.backend.dto;

public class TasksDTO {
    private Long id;
    private String title;

    public TasksDTO (Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
