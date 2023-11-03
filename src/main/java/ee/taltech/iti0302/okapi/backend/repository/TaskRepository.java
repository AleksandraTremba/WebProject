package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.repository.CrudRepository;
import ee.taltech.iti0302.okapi.backend.entities.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
