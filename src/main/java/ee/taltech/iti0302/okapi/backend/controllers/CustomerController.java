package ee.taltech.iti0302.okapi.backend.controllers;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.services.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "http://127.0.0.1:5173")
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("public/customers/{username}")
    public CustomerDTO getData(@PathVariable String username) {
        return customerService.getCustomerData(username);
    }

    @PostMapping("public/customers/login")
    public String login(@RequestBody CustomerDTO customer) {
        return customerService.login(customer);
    }

    @PutMapping("public/customers/register")
    public CustomerDTO registerCustomer(@RequestBody @Valid CustomerDTO customer) {
        return customerService.register(customer);
    }

    @PostMapping("customers/update/username")
    public CustomerDTO updateCustomerUsername(@RequestBody CustomerDTO customer) {
        return customerService.updateUsername(customer);
    }

    @PostMapping("customers/update/password")
    public CustomerDTO updateCustomerPassword(@RequestBody CustomerDTO customer) {
        return customerService.updatePassword(customer);
    }

    @DeleteMapping("customers/delete")
    public CustomerDTO deleteCustomer(@RequestBody CustomerDTO customer) {
        return customerService.delete(customer);
    }
}
