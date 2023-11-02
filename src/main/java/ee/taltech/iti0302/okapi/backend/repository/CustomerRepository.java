package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.repository.CrudRepository;

import ee.taltech.iti0302.okapi.backend.entities.Customer;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    boolean existsByUsername(String username);
    Optional<Customer> findByUsername(String username);
}
