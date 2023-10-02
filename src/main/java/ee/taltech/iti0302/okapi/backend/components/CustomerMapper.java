package ee.taltech.iti0302.okapi.backend.components;

import org.springframework.stereotype.Component;

import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;

@Component
public class CustomerMapper {
    public CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(customer.getUsername(), customer.getPassword());
    }

    public Customer toCustomer(CustomerDTO dto) {
        return new Customer(dto.getUsername(), dto.getPassword());
    }
}
