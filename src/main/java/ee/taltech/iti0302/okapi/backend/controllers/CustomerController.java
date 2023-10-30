package ee.taltech.iti0302.okapi.backend.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.services.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class CustomerController {
    @NonNull private CustomerRepository customerRepository;
    @NonNull private CustomerService customerService;

    @PostMapping("login")
    public boolean login(@RequestBody CustomerDTO customer) {
        return customerService.login(customer);
    }

    @PostMapping("register")
    public CustomerDTO registerCustomer(@RequestBody CustomerDTO customer) {
        return customerService.register(customer);
    }

    @PostMapping("update/username")
    public CustomerDTO updateCustomerUsername(@RequestBody CustomerDTO customer) {
        return customerService.updateUsername(customer);
    }

    @PostMapping("update/password")
    public CustomerDTO updateCustomerPassword(@RequestBody CustomerDTO customer) {
        return customerService.updatePassword(customer);
    }

    @DeleteMapping("delete")
    public CustomerDTO deleteCustomer(@RequestBody CustomerDTO customer) {
        return customerService.delete(customer);
    }
}
