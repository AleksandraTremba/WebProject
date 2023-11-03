package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    Optional<Timer> findById(long customerId);

    boolean existsById(long id);
}
