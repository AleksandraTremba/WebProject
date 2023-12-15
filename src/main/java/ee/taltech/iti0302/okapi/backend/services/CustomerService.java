package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerChangeDataDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerInitDTO;
import ee.taltech.iti0302.okapi.backend.enums.CustomerServiceUpdate;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.exceptions.ApplicationRuntimeException;
import ee.taltech.iti0302.okapi.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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

    private static final String errorInfo = "Provided username or password are invalid!";

    public boolean customerExists(String username) {
        return customerRepository.existsByUsername(username);
    }

    public List<Customer> findByGroupId(Long id) {
        return customerRepository.findByGroupId(id);
    }
    public Long getCustomerIdByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        return customer != null ? customer.getId() : null;
    }
    public void updateCustomerGroupData(Long id, Long groupId, GroupRoles role) throws NullPointerException {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null)
            throw new NullPointerException("It looks like customer with such ID does not exist!");

        customer.setGroupId(groupId);
        customer.setGroupRole(role);
        customerRepository.save(customer);
    }

    public void removeCustomerGroupData(Long id) throws NullPointerException {
        updateCustomerGroupData(id, null, null);
    }

    public void removeCustomerGroupData(Customer customer) {
        customer.setGroupId(null);
        customer.setGroupRole(null);

        customerRepository.save(customer);
    }
    public boolean customerIsGroupAdmin(Long id) throws NullPointerException {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null)
            throw new NullPointerException("ID is invalid");

        return customer.getGroupRole().equals(GroupRoles.ADMIN);
    }
    public CustomerDTO getCustomerData(String username) {
        Optional<Customer> dataShell = customerRepository.findByUsername(username);
        return dataShell.map(CustomerMapper.INSTANCE::toDTO).orElse(null);
    }
    public CustomerDTO login(CustomerInitDTO request) throws ApplicationRuntimeException {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null)
            throw new ApplicationRuntimeException(errorInfo);
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            throw new ApplicationRuntimeException(errorInfo);

        String token = tokenProvider.generateToken(request.getUsername());
        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(customer);
        dto.setToken(token);
        return dto;
    }
    public CustomerDTO register(CustomerInitDTO request) throws ApplicationRuntimeException {
        if (customerExists(request.getUsername()))
            throw new ApplicationRuntimeException("Customer with such username already exists!");

        Customer customer = CustomerMapper.INSTANCE.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);
        customer.setTimerId(timerService.createTimer(customer.getId()));
        customerRepository.save(customer);

        return CustomerMapper.INSTANCE.toDTO(customer);
    }

    private CustomerDTO update(CustomerChangeDataDTO request, CustomerServiceUpdate updateType) throws ApplicationRuntimeException {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);
        if (customer == null || !passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            throw new ApplicationRuntimeException(errorInfo);

        if (updateType.equals(CustomerServiceUpdate.CHANGE_USERNAME))
            customer.setUsername(request.getNewData());
        if (updateType.equals(CustomerServiceUpdate.CHANGE_PASSWORD))
            customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);
        return CustomerMapper.INSTANCE.toDTO(customer);
    }
    public CustomerDTO updateUsername(CustomerChangeDataDTO request) throws ApplicationRuntimeException {
        // Check whether the desired username is taken
        if (customerExists(request.getNewData()))
            throw new ApplicationRuntimeException("Customer with such username already exists!");
        return update(request, CustomerServiceUpdate.CHANGE_USERNAME);
    }
    public CustomerDTO updatePassword(CustomerChangeDataDTO request) {
        return update(request, CustomerServiceUpdate.CHANGE_PASSWORD);
    }
    public boolean delete(CustomerInitDTO request) throws ApplicationRuntimeException {
        Customer customer = customerRepository.findByUsername(request.getUsername()).orElse(null);

        if (customer == null || !passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            throw new ApplicationRuntimeException("Provided username or password are invalid!");

        timerService.deleteTimer(customer.getTimerId());
        customerRepository.deleteById(customer.getId());
        return true;
    }
}
