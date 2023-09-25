package ee.taltech.iti0302.okapi.backend.dto;

public class TasksDTO {
    private long id;
    private String title;
    private String description;

    public TasksDTO (long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }
}
