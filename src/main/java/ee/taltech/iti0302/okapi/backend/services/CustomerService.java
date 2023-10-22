package ee.taltech.iti0302.okapi.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public boolean customerExists(Customer customer) {
        return customerRepository.existsByUsername(customer.getUsername());
    }

    public void register(Customer customer) {
        customerRepository.save(customer);
    }
}
