package ee.taltech.iti0302.okapi.backend.repository;
import ee.taltech.iti0302.okapi.backend.entities.Affirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffirmationRepository extends JpaRepository<Affirmation, Long> { }
