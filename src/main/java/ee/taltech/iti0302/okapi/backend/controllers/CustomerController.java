package ee.taltech.iti0302.okapi.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.services.CustomerService;

@RestController
@RequestMapping("api/users")
public class CustomerController {
    
    @Autowired
    private CustomerRepository userRepository;
    private CustomerService customerService;

    public CustomerController(CustomerRepository userRepository, CustomerService customerService) {
        this.userRepository = userRepository;
        this.customerService = customerService;
    }

    @PostMapping("login")
    public boolean login(@RequestBody CustomerDTO customer) {
        return userRepository.existsByUsername(customer.getUsername());
    }

    @PostMapping("register")
    public CustomerDTO registerCustomer(@RequestBody CustomerDTO customer) {
        if (!customerService.customerExists(CustomerMapper.INSTANCE.toEntity(customer))) {
            customerService.register(CustomerMapper.INSTANCE.toEntity(customer));
            return customer;
        }
        return null;
    }
}
