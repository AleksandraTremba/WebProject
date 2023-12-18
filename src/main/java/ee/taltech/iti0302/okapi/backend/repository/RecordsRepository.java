package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Records;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordsRepository extends JpaRepository<Records, Long> {
}
