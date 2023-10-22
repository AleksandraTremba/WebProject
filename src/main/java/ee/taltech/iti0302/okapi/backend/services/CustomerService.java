package ee.taltech.iti0302.okapi.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public boolean customerExists(Customer customer) {
        return customerRepository.existsByUsername(customer.getUsername());
    }

    public void register(Customer customer) {
        customerRepository.save(customer);
    }
}
