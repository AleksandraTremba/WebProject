package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupCreateDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RecordsService recordsService;

    @InjectMocks
    private GroupService groupService;

    private GroupDTO buildGroupDTO(){
        return GroupDTO.builder()
                .id(1L)
                .name("Group Name")
                .adminUsername("admin")
                .build();
    }

    private GroupCreateDTO buildGroupCreateDTO(){
        return GroupCreateDTO.builder()
                .name("Group name")
                .adminId(1L)
                .build();
    }

    private Group buildGroup(){
        return new Group();
    }

    private Customer buildCustomer(String username) {
        return new Customer(username, "Pass");
    }


    private CustomerDTO buildCustomerDTO() {
        return CustomerDTO.builder()
                .id(null)
                .username("testUser")
                .timerId(null)
                .token("123")
                .build();
    }

    @Test
    public void testGetGroupById_ExistingGroup() {
        Group group = buildGroup();

        when(groupRepository.findById(123L)).thenReturn(Optional.of(group));

        GroupDTO resultDTO = groupService.getGroupById(123);

        assertNull(resultDTO.getId());
    }

    @Test
    public void testGetGroupById_NonExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO();

        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.empty());

        GroupDTO resultDTO = groupService.getGroupById(groupDTO.getId());

        assertNull(resultDTO);
    }

    @Test
    public void testSearchGroupById_ExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO();
        Group group = buildGroup();
        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.of(group));

        GroupDTO resultDTO = groupService.searchGroupById(groupDTO.getId());

        assertNull(resultDTO.getId());
    }

    @Test
    public void testSearchGroupById_NonExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO();

        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.empty());

        GroupDTO resultDTO = groupService.searchGroupById(groupDTO.getId());

        assertNull(resultDTO);
    }

    @Test
    public void testGetAllGroups() {
        Group group1 = buildGroup();
        Group group2 = buildGroup();
        Group group3 = buildGroup();

        List<Group> mockGroupList = List.of(group1, group2, group3);

        when(groupRepository.findAll()).thenReturn(mockGroupList);

        List<GroupDTO> resultDTOs = groupService.getAllGroups();

        assertEquals(mockGroupList.size(), resultDTOs.size());

        for (int i = 0; i < mockGroupList.size(); i++) {
            assertEquals(mockGroupList.get(i).getId(), resultDTOs.get(i).getId());
            assertEquals(mockGroupList.get(i).getName(), resultDTOs.get(i).getName());
        }
    }

    @Test
    public void testGetAllGroups_EmptyList() {
        when(groupRepository.findAll()).thenReturn(List.of());

        List<GroupDTO> resultDTOs = groupService.getAllGroups();

        assertNotNull(resultDTOs);
        assertTrue(resultDTOs.isEmpty());
    }

    @Test
    public void testCreateGroupSuccess() {
        Long customerId = 1L;
        String groupName = "TestGroup";

        GroupCreateDTO groupCreateDTO = new GroupCreateDTO(groupName, customerId);
        Group group = GroupMapper.INSTANCE.toEntity(groupCreateDTO);

        when(groupRepository.save(any(Group.class))).thenReturn(group);

        GroupDTO result = groupService.createGroup(customerId, groupName);

        assertNotNull(result);
        verify(groupRepository, times(1)).save(any(Group.class));
        verify(customerService, times(1)).updateCustomerGroupData(customerId, group.getId(), GroupRoles.ADMIN);
    }

    @Test
    public void testAddCustomerToGroup_Success() {
        Long customerId = 1L;
        Group group = new Group();

        GroupDTO result = groupService.addCustomerToGroup(customerId, group);

        assertNotNull(result);
        verify(customerService, times(1)).updateCustomerGroupData(customerId, group.getId(), GroupRoles.USER);
    }

    @Test
    public void testRemoveCustomerFromGroup_Admin() {
        Long customerId = 1L;
        Group group = new Group();
        List<Customer> groupCustomers = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            Customer temporaryShell = new Customer();
            groupCustomers.add(temporaryShell);
        }

        when(customerService.customerIsGroupAdmin(customerId)).thenReturn(true);
        when(customerService.findByGroupId(null)).thenReturn(groupCustomers);

        GroupDTO result = groupService.removeCustomerFromGroup(customerId, group);

        assertNotNull(result);
    }

    @Test
    void testManipulateCreateGroupSuccess() {
        Mockito.when(customerService.getCustomerIdByUsername("testUser")).thenReturn(1L);
        Mockito.when(groupRepository.findByName("TestGroup")).thenReturn(Optional.empty());

        GroupDTO result = groupService.manipulateCustomerAndGroup(buildCustomerDTO(), "TestGroup", GroupCustomerActionType.CREATE);

        assertEquals("TestGroup", result.getName());
    }

    @Test
    void testManipulateCreateGroupCustomerNotFound() {
        Mockito.when(customerService.getCustomerIdByUsername("testUser")).thenReturn(null);

        GroupDTO result = groupService.manipulateCustomerAndGroup(buildCustomerDTO(), "TestGroup", GroupCustomerActionType.CREATE);

        assertNull(result);
    }

    @Test
    void testManipulateCreateGroupGroupExists() {
        Mockito.when(customerService.getCustomerIdByUsername("testUser")).thenReturn(1L);
        Mockito.when(groupRepository.findByName("TestGroup")).thenReturn(Optional.of(buildGroup()));

        GroupDTO result = groupService.manipulateCustomerAndGroup(buildCustomerDTO(), "TestGroup", GroupCustomerActionType.CREATE);

        assertEquals("TestGroup", result.getName());
    }

    @Test
    void testManipulateAddCustomerToGroupSuccess() {
        Mockito.when(customerService.getCustomerIdByUsername("testUser")).thenReturn(1L);
        Mockito.when(groupRepository.findByName("TestGroup")).thenReturn(Optional.of(buildGroup()));

        GroupDTO result = groupService.manipulateCustomerAndGroup(buildCustomerDTO(), "TestGroup", GroupCustomerActionType.ADD);

        assertNull(result.getName());
    }


    @Test
    public void testDeleteGroup() {
        long groupId = 1L;
        Customer customer1 = buildCustomer("Customer1");
        Customer customer2 = buildCustomer("Customer2");

        Mockito.when(customerService.findByGroupId(groupId)).thenReturn(Arrays.asList(customer1, customer2));

        groupService.deleteGroup(groupId);

        Mockito.verify(customerService, times(1)).removeCustomerGroupData(customer1);
        Mockito.verify(customerService, times(1)).removeCustomerGroupData(customer2);
        Mockito.verify(groupRepository, times(1)).deleteById(groupId);
    }
}
