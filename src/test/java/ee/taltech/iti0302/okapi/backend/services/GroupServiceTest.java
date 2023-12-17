package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupCreateDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private GroupService groupService;

    private GroupDTO buildGroupDTO(Long id, String name, String adminUsername){
        return GroupDTO.builder()
                .id(id)
                .name(name)
                .adminUsername(adminUsername)
                .build();
    }

    private GroupCreateDTO buildGroupCreateDTO(String name, Long adminId){
        return GroupCreateDTO.builder()
                .name(name)
                .adminId(adminId)
                .build();
    }

    private Group buildGroup(Long id, String name, Long adminId){
        return new Group();
    }

    private Customer buildCustomer(String username, String password) {
        return new Customer(username, password);
    }


    private CustomerDTO buildCustomerDTO(Long id, String username, Long timerId, String token) {
        return CustomerDTO.builder()
                .id(id)
                .username(username)
                .timerId(timerId)
                .token(token)
                .build();
    }

    @Test
    public void testGetGroupById_ExistingGroup() {
        Group group = buildGroup(123L, "Test Group", 1L);

        when(groupRepository.findById(123L)).thenReturn(Optional.of(group));

        GroupDTO resultDTO = groupService.getGroupById(123);

        assertNull(resultDTO.getId());
    }

    @Test
    public void testGetGroupById_NonExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO(1L, "Group Name", "admin");

        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.empty());

        GroupDTO resultDTO = groupService.getGroupById(groupDTO.getId());

        assertNull(resultDTO);
    }

    @Test
    public void testSearchGroupById_ExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO(1L, "Group Name", "admin");
        Group group = buildGroup(1L, "Group Name", 1L);
        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.of(group));

        GroupDTO resultDTO = groupService.searchGroupById(groupDTO.getId());

        assertNull(resultDTO.getId());
    }

    @Test
    public void testSearchGroupById_NonExistingGroup() {
        GroupDTO groupDTO = buildGroupDTO(1L, "Group Name", "admin");

        when(groupRepository.findById(groupDTO.getId())).thenReturn(Optional.empty());

        GroupDTO resultDTO = groupService.searchGroupById(groupDTO.getId());

        assertNull(resultDTO);
    }

    @Test
    public void testGetAllGroups() {
        Group group1 = buildGroup(1L, "Group1", 2L);
        Group group2 = buildGroup(3L, "Group2", 4L);
        Group group3 = buildGroup(5L, "Group3", 6L);

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
    public void testCreateGroup() {
        GroupCreateDTO groupCreateDTO = buildGroupCreateDTO("Group name", 1L);

        when(groupRepository.save(Mockito.any())).thenAnswer(invocation -> {
            Group savedGroup = invocation.getArgument(0);
            savedGroup.setId(1L);
            return savedGroup;
        });

        groupService.createGroup(groupCreateDTO.getAdminId(), groupCreateDTO.getName());

        verify(groupRepository).save(Mockito.any());
    }

    @Test
    void testAddCustomerToGroup() {
        Long customerId = 1L;
        Group group = buildGroup(1L, "Group", 2L);

        Mockito.when(customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.USER))
                .thenReturn(true);

        GroupDTO result = groupService.addCustomerToGroup(customerId, group);

        assertEquals(group.getId(), result.getId());
    }

    @Test
    void testRemoveCustomerFromGroupAsGroupAdmin() {
        Long customerId = 1L;
        Group group = buildGroup(1L, "Group", 2L);

        Mockito.when(customerService.customerIsGroupAdmin(customerId)).thenReturn(true);
        Mockito.doNothing().when(groupService).deleteGroup(group.getId());

        GroupDTO result = groupService.removeCustomerFromGroup(customerId, group);

        assertEquals(group.getId(), result.getId());
    }

    @Test
    void testRemoveCustomerFromGroupAsNonGroupAdmin() {
        Long customerId = 1L;
        Group group = buildGroup(1L, "Group", 2L);

        Mockito.when(customerService.customerIsGroupAdmin(customerId)).thenReturn(false);
        Mockito.doNothing().when(customerService).removeCustomerGroupData(customerId);

        GroupDTO result = groupService.removeCustomerFromGroup(customerId, group);

        assertEquals(group.getId(), result.getId());
    }

    @Test
    public void testManipulateCustomerAndGroup() {
        CustomerDTO customerDTO = buildCustomerDTO(null, "username", null, "123");
        String groupName = "TestGroup";
        GroupCustomerActionType action = GroupCustomerActionType.CREATE;
        Long customerId = 1L;

        Mockito.when(customerService.getCustomerIdByUsername(customerDTO.getUsername())).thenReturn(customerId);
        Mockito.when(groupRepository.findByName(groupName)).thenReturn(java.util.Optional.empty());

        GroupDTO result = groupService.manipulateCustomerAndGroup(customerDTO, groupName, action);

        // Verify that createGroup was called on groupService
        Mockito.verify(groupService, Mockito.times(1)).createGroup(Mockito.eq(customerId), Mockito.eq(groupName));

        // You can also verify interactions with other mocks if needed
        Mockito.verify(customerService, Mockito.times(1)).getCustomerIdByUsername(Mockito.eq(customerDTO.getUsername()));
        Mockito.verify(groupRepository, Mockito.times(1)).findByName(Mockito.eq(groupName));
    }

    @Test
    public void testDeleteGroup() {
        long groupId = 1L;
        Customer customer1 = buildCustomer("Customer1", "Pass");
        Customer customer2 = buildCustomer("Customer2", "Pass");


        Mockito.when(customerService.findByGroupId(groupId)).thenReturn(Arrays.asList(customer1, customer2));

        groupService.deleteGroup(groupId);

        Mockito.verify(customerService, Mockito.times(1)).removeCustomerGroupData(customer1);
        Mockito.verify(customerService, Mockito.times(1)).removeCustomerGroupData(customer2);
        Mockito.verify(groupRepository, Mockito.times(1)).deleteById(groupId);
    }
}
