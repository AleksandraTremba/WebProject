package ee.taltech.iti0302.okapi.backend.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.services.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://127.0.0.1:5173")
public class CustomerController {
    @NonNull
    private CustomerService customerService;

    @GetMapping("{username}")
    public CustomerDTO getData(@PathVariable String username) {
        return customerService.getCustomerData(username);
    }

    @PostMapping("login")
    public boolean login(@RequestBody CustomerDTO customer) {
        return customerService.login(customer);
    }

    @PutMapping("register")
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
