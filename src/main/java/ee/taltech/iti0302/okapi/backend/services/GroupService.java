package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupCreateDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final CustomerService customerService;

    private GroupDTO getGroupById(long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        return group.map(GroupMapper.INSTANCE::toDTO).orElse(null);
    }

    public GroupDTO searchGroupById(long groupId) {
        return getGroupById(groupId);
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> group = groupRepository.findAll();
        return group.stream()
                .map(GroupMapper.INSTANCE::toDTO)
                .toList();
    }

    private GroupDTO createGroup(Long customerId, String groupName) {
        GroupCreateDTO temporaryShell = new GroupCreateDTO(groupName, customerId);
        Group group = GroupMapper.INSTANCE.toEntity(temporaryShell);

        groupRepository.save(group);

        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.ADMIN);

        return GroupMapper.INSTANCE.toDTO(group);
    }

    private GroupDTO addCustomerToGroup(Long customerId, Group group) {
        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.USER);
        return GroupMapper.INSTANCE.toDTO(group);
    }

    private GroupDTO removeCustomerFromGroup(Long customerId, Group group) {
        if (customerService.customerIsGroupAdmin(customerId))
            deleteGroup(group.getId());
        else
            customerService.removeCustomerGroupData(customerId);
        return GroupMapper.INSTANCE.toDTO(group);
    }

    public GroupDTO manipulateCustomerAndGroup(CustomerDTO dto, String groupName, GroupCustomerActionType action) {
        Long customerId = customerService.getCustomerIdByUsername(dto.getUsername());
        if (customerId == null)
            return null;

        if (action.equals(GroupCustomerActionType.CREATE))
            return createGroup(customerId, groupName);
        else {
            Group group = groupRepository.findByName(groupName).orElse(null);
            if (group == null)
                return null;

            if (action.equals(GroupCustomerActionType.ADD))
                return addCustomerToGroup(customerId, group);
            if (action.equals(GroupCustomerActionType.DELETE))
                return removeCustomerFromGroup(customerId, group);
        }

        return null;
    }

    public void deleteGroup(long groupId) {
        List<Customer> groupUsers = customerService.findByGroupId(groupId);
        for (Customer customer : groupUsers)
            customerService.removeCustomerGroupData(customer);
        groupRepository.deleteById(groupId);
    }
}

