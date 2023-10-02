package ee.taltech.iti0302.okapi.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public boolean login(@RequestBody CustomerDTO customer) {
        return userRepository.existsByUsername(customer.getUsername());
    }

    @PostMapping("register")
    public boolean registerCustomer(@RequestBody CustomerDTO customer) {
        if (!userRepository.existsByUsername(customer.getUsername())) {
            userRepository.save(new Customer(customer.getUsername(), customer.getPassword()));
            return false;
        }
        return true;
    }
}
