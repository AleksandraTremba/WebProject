package ee.taltech.iti0302.okapi.backend.repository;


import ee.taltech.iti0302.okapi.backend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
