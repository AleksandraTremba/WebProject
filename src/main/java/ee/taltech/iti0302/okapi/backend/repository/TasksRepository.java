package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.repository.CrudRepository;
import ee.taltech.iti0302.okapi.backend.entities.Tasks;

public interface TasksRepository extends CrudRepository<Tasks, Long> {

}
