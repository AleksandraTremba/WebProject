package ee.taltech.iti0302.okapi.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

@RestController
@RequestMapping("api/users")
public class CustomerController {
    
    @Autowired
    private CustomerRepository userRepository;

    public CustomerController(CustomerRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("login")
    public boolean login(@RequestParam CustomerDTO customer) {
        boolean exists = userRepository.existsByUsername(customer.getUsername());
        return exists;
    }

    @PostMapping("register")
    public Customer registerCustomer(@RequestBody CustomerDTO customer) {
        return userRepository.save(new Customer(customer.getUsername(), customer.getPassword()));
    }
}
