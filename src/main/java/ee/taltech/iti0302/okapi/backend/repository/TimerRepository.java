package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerRepository extends JpaRepository<Timer, Long> {

}
