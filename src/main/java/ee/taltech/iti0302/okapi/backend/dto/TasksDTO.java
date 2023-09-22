package ee.taltech.iti0302.okapi.backend.dto;

public class TasksDTO {
    private Long id;
    private String title;
    private String description;

    public TasksDTO (Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public TasksDTO() {

    }


    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }
}
