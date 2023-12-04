package ee.taltech.iti0302.okapi.backend.repository;

import ee.taltech.iti0302.okapi.backend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import ee.taltech.iti0302.okapi.backend.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    Optional<Customer> findByUsername(String username);
    List<Customer> findByGroupId(long groupId);
}
