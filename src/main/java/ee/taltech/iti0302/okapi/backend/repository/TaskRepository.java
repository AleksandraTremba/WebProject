package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ee.taltech.iti0302.okapi.backend.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
