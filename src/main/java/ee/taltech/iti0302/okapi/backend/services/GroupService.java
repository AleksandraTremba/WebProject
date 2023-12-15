package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupCreateDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.debug("Searching for group with ID: {}", groupId);
        return getGroupById(groupId);
    }

    public List<GroupDTO> getAllGroups() {
        log.debug("Retrieving all groups");
        List<Group> group = groupRepository.findAll();
        List<GroupDTO> groupDTOs = group.stream()
                .map(GroupMapper.INSTANCE::toDTO)
                .toList();

        log.info("Retrieved {} groups.", groupDTOs.size());
        return groupDTOs;
    }

    private GroupDTO createGroup(Long customerId, String groupName) {
        log.info("Creating group: {} for customer with ID: {}", groupName, customerId);
        GroupCreateDTO temporaryShell = new GroupCreateDTO(groupName, customerId);
        Group group = GroupMapper.INSTANCE.toEntity(temporaryShell);

        groupRepository.save(group);

        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.ADMIN);

        log.info("Group created successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    private GroupDTO addCustomerToGroup(Long customerId, Group group) {
        log.info("Adding customer with ID: {} to group with ID: {}", customerId, group.getId());
        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.USER);
        log.info("Customer added to group successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    private GroupDTO removeCustomerFromGroup(Long customerId, Group group) {
        log.info("Removing customer with ID: {} from group with ID: {}", customerId, group.getId());
        if (customerService.customerIsGroupAdmin(customerId)) {
            deleteGroup(group.getId());
        } else {
            customerService.removeCustomerGroupData(customerId);
        }
        log.info("Customer removed from group successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    public GroupDTO manipulateCustomerAndGroup(CustomerDTO dto, String groupName, GroupCustomerActionType action) {
        Long customerId = customerService.getCustomerIdByUsername(dto.getUsername());
        if (customerId == null) {
            log.warn("Could not find customer with username: {}", dto.getUsername());
            return null;
        }

        if (action.equals(GroupCustomerActionType.CREATE)) {
            return createGroup(customerId, groupName);
        } else {
            Group group = groupRepository.findByName(groupName).orElse(null);
            if (group == null) {
                log.warn("Could not find group with name: {}", groupName);
                return null;
            }

            if (action.equals(GroupCustomerActionType.ADD)) {
                return addCustomerToGroup(customerId, group);
            }
            if (action.equals(GroupCustomerActionType.DELETE)) {
                return removeCustomerFromGroup(customerId, group);
            }
        }

        return null;
    }

    public void deleteGroup(long groupId) {
        log.info("Deleting group with ID: {}", groupId);
        List<Customer> groupUsers = customerService.findByGroupId(groupId);
        for (Customer customer : groupUsers) {
            customerService.removeCustomerGroupData(customer);
        }
        groupRepository.deleteById(groupId);
        log.info("Group deleted successfully. Group ID: {}", groupId);
    }
}
