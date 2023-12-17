package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.CustomerMapper;
import ee.taltech.iti0302.okapi.backend.components.CustomerMapperImpl;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerChangeDataDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerInitDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.enums.RecordType;
import ee.taltech.iti0302.okapi.backend.exceptions.ApplicationRuntimeException;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private TimerService timerService;
    @Mock
    private RecordsService recordsService;
    @Spy
    private CustomerMapper customerMapper = new CustomerMapperImpl();
    @InjectMocks
    private CustomerService customerService;

    private static final String errorInfo = "Provided username or password are invalid!";
    private static final String errorCustomerExists = "Customer with such username already exists!";

    // #### BUILDING WITH SPECIFIED VALUES ####
    private CustomerInitDTO buildCustomerInitDTO(String username, String password) {
        return new CustomerInitDTO(username, password);
    }

    private CustomerChangeDataDTO buildCustomerChangeDataDTO(String username, String password, String newData) {
        return CustomerChangeDataDTO.builder()
                .username(username)
                .password(password)
                .newData(newData)
                .build();
    }

    private CustomerDTO buildCustomerDTO(Long id, String username, Long timerId, String token) {
        return CustomerDTO.builder()
                .id(id)
                .username(username)
                .timerId(timerId)
                .token(token)
                .build();
    }

    private Customer buildCustomer(String username, String password) {
        return new Customer(username, password);
    }

    // #### BUILDING WITH DEFAULT VALUES ####
    private CustomerInitDTO buildCustomerInitDTOWithDefaultValues() {
        return buildCustomerInitDTO("test", "test");
    }

    private CustomerChangeDataDTO buildCustomerChangeDataDTOWithDefaultValues() {
        return buildCustomerChangeDataDTO("test", "test", "test2");
    }

    private CustomerDTO buildCustomerDTOWithDefaultValues() {
        return buildCustomerDTO(null, "test", null, "123");
    }

    private Customer buildCustomerWithDefaultValues() {
        return buildCustomer("test", "test");
    }

    // ###### REGISTRATION ######
    @Test
    void registerWithEmptyUsernameThrowsError() {
        CustomerInitDTO request = buildCustomerInitDTO(null, "test");
        assertThrows(NullPointerException.class, () -> customerService.register(request));
    }

    @Test
    void registerWithEmptyPasswordThrowsError() {
        CustomerInitDTO request = buildCustomerInitDTO("test", null);
        assertThrows(NullPointerException.class, () -> customerService.register(request));
    }

    @Test
    void registerUsernameExistsError() {
        String username = "test";

        // Given
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        given(customerRepository.existsByUsername(username)).willReturn(true);

        // When
        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.register(request));

        // Then
        assertEquals("Customer with such username already exists!", exception.getMessage());
    }

    @Test
    void registerSuccessfully() {
        String username = "test";
        String password = "test";
        Long timerId = 1L;

        // Given
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();

        CustomerDTO dto = buildCustomerDTOWithDefaultValues();
        dto.setTimerId(timerId);
        dto.setToken(null);

        Customer customer = buildCustomerWithDefaultValues();

        given(customerRepository.existsByUsername(username)).willReturn(false);
        given(passwordEncoder.encode(password)).willReturn(password);
        given(timerService.createTimer(customer.getId())).willReturn(timerId);


        // When
        CustomerDTO response = customerService.register(request);

        // Then
        then(customerRepository).should().existsByUsername(username);
        then(passwordEncoder).should().encode(password);
        then(timerService).should().createTimer(customer.getId());
        assertEquals(dto, response);
    }

    // ###### LOGIN ######
    @Test
    void loginWithEmptyUsernameThrowsError() {
        CustomerInitDTO request = buildCustomerInitDTO(null, "test");
        assertThrows(ApplicationRuntimeException.class, () -> customerService.login(request));
    }

    @Test
    void loginWithEmptyPasswordThrowsError() {
        CustomerInitDTO request = buildCustomerInitDTO("test", null);
        assertThrows(ApplicationRuntimeException.class, () -> customerService.login(request));
    }

    @Test
    void loginCustomerIsNotRegistered() {
        String username = "test";

        // Given
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        given(customerRepository.findByUsername(username)).willReturn(Optional.empty());

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.login(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void loginPasswordIsInvalid() {
        String username = "test";
        String password = "test";

        // Given
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        Customer customer = buildCustomer("test", "test123");

        given(customerRepository.findByUsername(username)).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(false);

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.login(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void loginSuccessfully() {
        String username = "test";
        String password = "test";
        String token = "123";

        // Given
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        CustomerDTO expectedResponse = buildCustomerDTOWithDefaultValues();
        Customer customer = buildCustomerWithDefaultValues();

        given(customerRepository.findByUsername(username)).willReturn(Optional.of(customer));
        given(tokenProvider.generateToken(username)).willReturn(token);
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(true);

        CustomerDTO actualResponse = customerService.login(request);

        then(customerRepository).should().findByUsername(request.getUsername());
        then(tokenProvider).should().generateToken(request.getUsername());
        assertEquals(expectedResponse, actualResponse);
    }

    // ###### UPDATING USERNAME ######
    @Test
    void updateUsernameMustThrowUsernameIsTakenError() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();

        given(customerRepository.existsByUsername("test2")).willReturn(true);

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.updateUsername(request));
        assertEquals(errorCustomerExists, exception.getMessage());
    }

    @Test
    void updateUsernameCustomerDoesNotExist() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();

        given(customerRepository.existsByUsername("test2")).willReturn(false);
        given(customerRepository.findByUsername("test")).willReturn(Optional.empty());

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.updateUsername(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void updateUsernamePasswordIsInvalid() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();
        Customer customer = buildCustomer("test", "123");

        given(customerRepository.existsByUsername("test2")).willReturn(false);
        given(customerRepository.findByUsername("test")).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(false);

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.updateUsername(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void updateUsernameSuccessfully() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();

        CustomerDTO expectedResponse = buildCustomerDTOWithDefaultValues();
        expectedResponse.setUsername("test2");
        expectedResponse.setToken(null);

        Customer customer = buildCustomer("test", "123");

        given(customerRepository.existsByUsername("test2")).willReturn(false);
        given(customerRepository.findByUsername("test")).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(true);

        CustomerDTO response = customerService.updateUsername(request);
        assertEquals(expectedResponse, response);
    }

    // ###### UPDATE PASSWORD ######
    @Test
    void updatePasswordCustomerDoesNotExist() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();

        given(customerRepository.findByUsername("test")).willReturn(Optional.empty());

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.updatePassword(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void updatePasswordSuccessfully() {
        CustomerChangeDataDTO request = buildCustomerChangeDataDTOWithDefaultValues();

        CustomerDTO expectedResponse = buildCustomerDTOWithDefaultValues();
        expectedResponse.setToken(null);

        Customer customer = buildCustomer("test", "test");

        given(customerRepository.findByUsername("test")).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getPassword())).willReturn(request.getNewData());

        CustomerDTO response = customerService.updatePassword(request);
        assertEquals(expectedResponse, response);
        assertEquals(request.getNewData(), customer.getPassword());
    }

    // ###### CUSTOMER ACCOUNT ERASURE ######
    @Test
    void deleteAccountCustomerDoesNotExist() {
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();

        given(customerRepository.findByUsername(request.getUsername())).willReturn(Optional.empty());

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.delete(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void deleteAccountPasswordIsInvalid() {
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        Customer customer = buildCustomerWithDefaultValues();

        given(customerRepository.findByUsername(request.getUsername())).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(false);

        Throwable exception = assertThrows(ApplicationRuntimeException.class, () -> customerService.delete(request));
        assertEquals(errorInfo, exception.getMessage());
    }

    @Test
    void deleteAccountSuccessfully() {
        CustomerInitDTO request = buildCustomerInitDTOWithDefaultValues();
        Customer customer = buildCustomerWithDefaultValues();
        customer.setTimerId(1L);

        given(customerRepository.findByUsername(request.getUsername())).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(request.getPassword(), customer.getPassword())).willReturn(true);

        boolean response = customerService.delete(request);
        assertTrue(response);
    }

    // ###### LITTLE METHODS FOR BETTER WORK ######
    @Test
    void findCustomerByGroupIdNoCustomers() {
        List<Customer> customers = new ArrayList<>();
        Long groupId = 1L;

        for (int i = 0; i < 3; i++) {
            Customer customer = new Customer("test", "test");
            customer.setGroupId(20L);
            customers.add(customer);
        }

        given(customerRepository.findByGroupId(groupId)).willReturn(customers.stream()
                .filter(customer -> customer.getGroupId().equals(groupId))
                .toList());

        List<Customer> response = customerService.findByGroupId(groupId);
        assertTrue(response.isEmpty());
    }

    @Test
    void findCustomerByGroupIdFindsCustomer() {
        List<Customer> customers = new ArrayList<>();
        Long groupId = 1L;

        Customer requiredData = buildCustomerWithDefaultValues();
        requiredData.setGroupId(groupId);

        for (int i = 0; i < 3; i++) {
            Customer customer = buildCustomerWithDefaultValues();
            customer.setGroupId(20L);
            customers.add(customer);
        }
        customers.add(requiredData);

        given(customerRepository.findByGroupId(groupId)).willReturn(customers.stream()
                .filter(customer -> customer.getGroupId().equals(groupId))
                .toList());

        List<Customer> response = customerService.findByGroupId(groupId);

        assertEquals(1, response.size());
        assertTrue(response.contains(requiredData));
    }

    @Test
    void getCustomerIdByUsernameDoesNotExist() {
        given(customerRepository.findByUsername("test")).willReturn(Optional.empty());

        Long response = customerService.getCustomerIdByUsername("test");
        assertNull(response);
    }

    @Test
    void getCustomerIdByUsernameSuccess() {
        // Default value is null because it is set by database
        Customer customer = buildCustomerWithDefaultValues();
        given(customerRepository.findByUsername("test")).willReturn(Optional.of(customer));

        Long response = customerService.getCustomerIdByUsername("test");
        assertNull(response);
    }

    @Test
    void updateCustomerGroupDataIdInvalid() {
        given(customerRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> customerService.updateCustomerGroupData(1L, 1L, GroupRoles.ADMIN));
    }

    @Test
    void updateCustomerGroupDataSuccess() {
        Customer customer = buildCustomerWithDefaultValues();
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        assertDoesNotThrow(() -> customerService.updateCustomerGroupData(1L, 1L, GroupRoles.ADMIN));
        assertEquals(1L, customer.getGroupId());
        assertEquals(GroupRoles.ADMIN, customer.getGroupRole());
    }

    @Test
    void removeCustomerGroupDataSuccess() {
        Customer customer = buildCustomerWithDefaultValues();
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        assertDoesNotThrow(() -> customerService.removeCustomerGroupData(1L));
        assertNull(customer.getGroupId());
        assertNull(customer.getGroupRole());
    }
}