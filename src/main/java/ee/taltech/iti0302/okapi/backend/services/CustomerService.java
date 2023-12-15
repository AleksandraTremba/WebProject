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
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final TimerService timerService;

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
        }
    }

    public void removeCustomerGroupData(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setGroupId(null);
            customer.setGroupRole(null);

            customerRepository.save(customer);
        }
    }

    public void removeCustomerGroupData(Customer customer) {
        customer.setGroupId(null);
        customer.setGroupRole(null);

        customerRepository.save(customer);
    }

    public boolean customerIsGroupAdmin(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null)
            return customer.getGroupRole().equals(GroupRoles.ADMIN);
        return false;
    }

    public CustomerDTO getCustomerData(String username) {
        Optional<Customer> dataShell = customerRepository.findByUsername(username);
        return dataShell.map(CustomerMapper.INSTANCE::toDTO).orElse(null);
    }

    public CustomerDTO login(CustomerInitDTO request) {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null)
            return null;

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            return null;

        String token = tokenProvider.generateToken(request.getUsername());
        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(customer);
        dto.setToken(token);
        return dto;
    }
    
    public CustomerInitDTO register(CustomerInitDTO request) {
        if (customerExists(request.getUsername()))
            return null;

        Customer customer = CustomerMapper.INSTANCE.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);
        customer.setTimerId(timerService.createTimer(customer.getId()));
        customerRepository.save(customer);

        return request;
    }

    private CustomerChangeDataDTO update(CustomerChangeDataDTO request, CustomerServiceUpdate updateType) {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null)
            return null;

        if (updateType.equals(CustomerServiceUpdate.CHANGE_USERNAME))
            customer.setUsername(request.getNewData());
        if (updateType.equals(CustomerServiceUpdate.CHANGE_PASSWORD))
            customer.setPassword(request.getNewData());

        customerRepository.save(customer);
        return request;
    }

    public CustomerChangeDataDTO updateUsername(CustomerChangeDataDTO request) {
        // Check whether the desired username is taken
        if (customerExists(request.getNewData())) {
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
            return true;
        }

        return false;
    }
}
