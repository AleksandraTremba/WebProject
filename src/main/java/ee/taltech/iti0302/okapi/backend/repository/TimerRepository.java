package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface TimerRepository extends CrudRepository<Timer, Long> {
    Timer findById(long customerId);
    boolean existsById(long id);
}
