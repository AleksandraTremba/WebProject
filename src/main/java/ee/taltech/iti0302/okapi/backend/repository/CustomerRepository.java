package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.repository.CrudRepository;

import ee.taltech.iti0302.okapi.backend.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    boolean existsByUsername(String username);
    Customer findByUsername(String username);
}
