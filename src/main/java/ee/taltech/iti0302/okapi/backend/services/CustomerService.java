package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerChangeDataDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerInitDTO;
import ee.taltech.iti0302.okapi.backend.dto.timer.TimerDTO;
import ee.taltech.iti0302.okapi.backend.enums.CustomerServiceUpdate;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final TimerService timerService;

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public boolean customerExists(String username) {
        return customerRepository.existsByUsername(username);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public List<Customer> findByGroupId(Long id) {
        return customerRepository.findByGroupId(id);
    }

    public Long getCustomerIdByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        return customer != null ? customer.getId() : null;
    }

    public void updateCustomerGroupData(Long id, Long groupId, GroupRoles role) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setGroupId(groupId);
            customer.setGroupRole(role);
            customerRepository.save(customer);
            log.info("Updated customer group data. Customer ID: {}, Group ID: {}, Role: {}", id, groupId, role);
        }
    }

    public void removeCustomerGroupData(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setGroupId(null);
            customer.setGroupRole(null);

            customerRepository.save(customer);
            log.info("Removed customer group data. Customer ID: {}", id);
        }
    }

    public void removeCustomerGroupData(Customer customer) {
        customer.setGroupId(null);
        customer.setGroupRole(null);

        customerRepository.save(customer);
        log.info(getCurrentTime() + ": " + "Removed customer group data. Customer ID: {}", customer.getId());
    }

    public boolean customerIsGroupAdmin(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            boolean isGroupAdmin = customer.getGroupRole().equals(GroupRoles.ADMIN);
            log.debug(getCurrentTime() + ": " + "Customer with ID {} is a group admin: {}", id, isGroupAdmin);
            return isGroupAdmin;
        }
        return false;
    }

    public CustomerDTO getCustomerData(String username) {
        Optional<Customer> dataShell = customerRepository.findByUsername(username);
        CustomerDTO customerDTO = dataShell.map(CustomerMapper.INSTANCE::toDTO).orElse(null);
        log.debug(getCurrentTime() + ": " + "Retrieved customer data for username: {}", username);
        return customerDTO;
    }

    public CustomerDTO login(CustomerInitDTO request) {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null) {
            log.warn(getCurrentTime() + ": " + "Login failed. Customer not found with username: {}", request.getUsername());
            return null;
        }

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            log.warn(getCurrentTime() + ": " + "Login failed. Incorrect password for customer with username: {}", request.getUsername());
            return null;
        }

        String token = tokenProvider.generateToken(request.getUsername());
        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(customer);
        dto.setToken(token);
        log.info(getCurrentTime() + ": " + "Customer logged in successfully. Username: {}", request.getUsername());
        return dto;
    }

    public CustomerInitDTO register(CustomerInitDTO request) {
        if (customerExists(request.getUsername())) {
            log.warn(getCurrentTime() + ": " + "Registration failed. Customer with username {} already exists.", request.getUsername());
            return null;
        }

        Customer customer = CustomerMapper.INSTANCE.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);
        customer.setTimerId(timerService.createTimer(customer.getId()));
        customerRepository.save(customer);

        log.info(getCurrentTime() + ": " + "Customer registered successfully. Username: {}", request.getUsername());
        return request;
    }

    private CustomerChangeDataDTO update(CustomerChangeDataDTO request, CustomerServiceUpdate updateType) {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null) {
            log.warn(getCurrentTime() + ": " + "Update failed. Customer not found with username: {}", request.getUsername());
            return null;
        }

        if (updateType.equals(CustomerServiceUpdate.CHANGE_USERNAME)) {
            customer.setUsername(request.getNewData());
            log.info(getCurrentTime() + ": " + "Updated customer username. New username: {}", request.getNewData());
        }
        if (updateType.equals(CustomerServiceUpdate.CHANGE_PASSWORD)) {
            customer.setPassword(request.getNewData());
            log.info(getCurrentTime() + ": " + "Updated customer password. Customer ID: {}", customer.getId());
        }

        customerRepository.save(customer);
        return request;
    }

    public CustomerChangeDataDTO updateUsername(CustomerChangeDataDTO request) {
        // Check whether the desired username is taken
        if (customerExists(request.getNewData())) {
            log.info("Username update failed. Username {} is already taken.", request.getNewData());
            return null;
        }

        return update(request, CustomerServiceUpdate.CHANGE_USERNAME);
    }

    public CustomerChangeDataDTO updatePassword(CustomerChangeDataDTO request) {
        return update(request, CustomerServiceUpdate.CHANGE_PASSWORD);
    }

    public boolean delete(CustomerInitDTO request) {
        Optional<Customer> customerOptional = customerRepository.findByUsername(request.getUsername());
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customerRepository.deleteById(customer.getId());
            log.info(getCurrentTime() + ": " + "Customer deleted successfully. Username: {}", request.getUsername());
            return true;
        }

        log.warn(getCurrentTime() + ": " + "Deletion failed. Customer not found with username: {}", request.getUsername());
        return false;
    }
}
