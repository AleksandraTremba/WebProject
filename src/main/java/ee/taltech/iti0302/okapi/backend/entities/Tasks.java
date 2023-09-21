package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    public Tasks(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Tasks() {

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
}