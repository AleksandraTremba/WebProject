package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface TimerRepository extends CrudRepository<Timer, Long> {
    Optional<Timer> findById(long customerId);

    boolean existsById(long id);
}
