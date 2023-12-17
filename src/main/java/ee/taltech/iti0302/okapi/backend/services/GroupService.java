package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupCreateDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.enums.RecordType;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final CustomerService customerService;
    private final RecordsService recordsService;

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    protected GroupDTO getGroupById(long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        return group.map(GroupMapper.INSTANCE::toDTO).orElse(null);
    }

    public GroupDTO searchGroupById(long groupId) {
        log.info(getCurrentTime() + ": " + "Searching for group with ID: {}", groupId);
        return getGroupById(groupId);
    }

    public List<GroupDTO> getAllGroups() {
        log.debug(getCurrentTime() + ": " + "Retrieving all groups");
        List<Group> group = groupRepository.findAll();
        List<GroupDTO> groupDTOs = group.stream()
                .map(GroupMapper.INSTANCE::toDTO)
                .toList();

        log.info(getCurrentTime() + ": " + "Retrieved {} groups.", groupDTOs.size());
        return groupDTOs;
    }

    public GroupDTO createGroup(Long customerId, String groupName) {
        log.debug(getCurrentTime() + ": " + "Creating group: {} for customer with ID: {}", groupName, customerId);
        GroupCreateDTO temporaryShell = new GroupCreateDTO(groupName, customerId);
        Group group = GroupMapper.INSTANCE.toEntity(temporaryShell);

        groupRepository.save(group);

        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.ADMIN);
        recordsService.updateRecords(RecordType.GROUPS);

        log.info(getCurrentTime() + ": " + "Group created successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    public GroupDTO addCustomerToGroup(Long customerId, Group group) {
        customerService.updateCustomerGroupData(customerId, group.getId(), GroupRoles.USER);
        log.info(getCurrentTime() + ": " + "Customer added to group successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    public GroupDTO removeCustomerFromGroup(Long customerId, Group group) {
        if (customerService.customerIsGroupAdmin(customerId)) {
            deleteGroup(group.getId());
        } else {
            customerService.removeCustomerGroupData(customerId);
        }
        log.info(getCurrentTime() + ": " + "Customer removed from group successfully. Group ID: {}", group.getId());
        return GroupMapper.INSTANCE.toDTO(group);
    }

    public GroupDTO manipulateCustomerAndGroup(CustomerDTO dto, String groupName, GroupCustomerActionType action) {
        Long customerId = customerService.getCustomerIdByUsername(dto.getUsername());
        if (customerId == null) {
            log.warn(getCurrentTime() + ": " + "Could not find customer with username: {}", dto.getUsername());
            return null;
        }

        if (action.equals(GroupCustomerActionType.CREATE)) {
            return createGroup(customerId, groupName);
        } else {
            Group group = groupRepository.findByName(groupName).orElse(null);
            if (group == null) {
                log.warn(getCurrentTime() + ": " + "Could not find group with name: {}", groupName);
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

    public void deleteGroup(Long groupId) {
        List<Customer> groupUsers = customerService.findByGroupId(groupId);
        for (Customer customer : groupUsers) {
            customerService.removeCustomerGroupData(customer);
        }
        groupRepository.deleteById(groupId);
        log.info(getCurrentTime() + ": " + "Group deleted successfully. Group ID: {}", groupId);
    }
}
