package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.components.CustomerServiceUpdate;
import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RecordsRepository recordsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    private boolean customerExists(String username) {
        return customerRepository.existsByUsername(username);
    }

    private boolean validPassword(Customer customer, String testPassword) {
        return customer.getPassword().equals(testPassword);
    }

    private void eraseCustomerPassword(CustomerDTO dto) {
        dto.setPassword("");
    }

    public CustomerDTO getCustomerData(String username) {
        Optional<Customer> dataShell = customerRepository.findByUsername(username);
        if (dataShell.isPresent()) {
            CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(dataShell.get());
            eraseCustomerPassword(dto);
            return dto;
        }
        return null;
    }

    public String login(CustomerDTO request) {
        Optional<Customer> opCustomer = customerRepository.findById(request.getId());
        if (opCustomer.isEmpty())
            return null;

        Customer customer = opCustomer.get();
        if (passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            return tokenProvider.generateToken(request.getUsername());

        return null;
    }
    
    public CustomerDTO register(CustomerDTO customerDTO) {
        if (customerExists(customerDTO.getUsername()))
            return null;

        Customer customer = CustomerMapper.INSTANCE.toEntity(customerDTO);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerRepository.save(customer);
        updateRecords();
        return customerDTO;
    }

    public CustomerDTO update(CustomerDTO customer, CustomerServiceUpdate updateType) {
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());
        if (customerOptional.isPresent()) {
            Customer dataShell = customerOptional.get();
            if (passwordEncoder.matches(customer.getPassword(), dataShell.getPassword())) {
                if (updateType.equals(CustomerServiceUpdate.CHANGE_USERNAME)) {
                    dataShell.setUsername(customer.getNewUsername());
                    customer.setUsername(customer.getNewUsername());
                    customerRepository.save(dataShell);

                    return customer;
                }

                if (updateType.equals(CustomerServiceUpdate.CHANGE_PASSWORD)) {
                    dataShell.setPassword(customer.getNewPassword());
                    customer.setPassword(customer.getNewPassword());
                    customerRepository.save(dataShell);

                    return customer;
                }
            }
        }

        return null;
    }

    public CustomerDTO updateUsername(CustomerDTO customer) {
        // Check whether the desired username is taken
        if (customerExists(customer.getNewUsername())) {
            return null;
        }

        return update(customer, CustomerServiceUpdate.CHANGE_USERNAME);
    }

    public CustomerDTO updatePassword(CustomerDTO customer) {
        return update(customer, CustomerServiceUpdate.CHANGE_PASSWORD);
    }

    public CustomerDTO delete(CustomerDTO customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());
        if (customerOptional.isPresent()) {
            Customer dataShell = customerOptional.get();
            if (validPassword(dataShell, customer.getPassword())) {
                customerRepository.deleteById(dataShell.getId());
                return customer;
            }
        }

        return null;
    }

    private void updateRecords() {
        Records records = recordsRepository.findById(1L).orElseGet(() -> {
            Records newRecords = new Records();
            recordsRepository.save(newRecords);
            return newRecords;
        });

        records.setNumberOfUsers(records.getNumberOfUsers() + 1);
        recordsRepository.save(records);
    }
}
