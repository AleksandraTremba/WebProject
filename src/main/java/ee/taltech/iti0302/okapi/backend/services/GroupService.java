package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.components.TimerMapper;
import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.GroupDTO;
import ee.taltech.iti0302.okapi.backend.dto.TimerDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.entities.Timer;
import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final CustomerRepository customerRepository;
    // TODO: 27.11.2023
    private final GroupMapper groupMapper;

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group group = GroupMapper.INSTANCE.toEntity(groupDTO);
        // TODO: 27.11.2023 replace getADmin.getId with token
        Customer customer = customerRepository.findById(groupDTO.getAdmin().getId()).orElseThrow();
        group.setAdmin(customer);
        groupRepository.save(group);

        customer.setGroup(group);
        customer.setGroupRole(GroupRoles.GroupRole.ADMIN);

        customerRepository.save(customer);

        // TODO: 27.11.2023  groupMapper.toDto
        return GroupMapper.INSTANCE.toDTO(group);

//        Group group = groupRepository.save(GroupMapper.INSTANCE.toEntity(dto));
//        dto.setId(group.getId());
//        if (group.getId() != null) {
//            return dto;
//        }
//        return null;
    }

    public GroupDTO getGroupById(long groupId) {
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

    public GroupDTO addUserToGroup(CustomerDTO customerDTO, long groupId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getId());
        Group group = groupRepository.findById(groupId).orElse(null);

        if (optionalCustomer.isPresent() && group != null) {
            Customer customer = optionalCustomer.get();
            customer.setGroup(group);
            if (customer.getGroupRole() != GroupRoles.GroupRole.ADMIN) {
                customer.setGroupRole(GroupRoles.GroupRole.USER);
            }
            customerRepository.save(customer);
            return GroupMapper.INSTANCE.toDTO(group);
        }
        return null;
    }

    public GroupDTO removeUserFromGroup(CustomerDTO customerDTO, long groupId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getId());
        Group group = groupRepository.findById(groupId).orElse(null);
        if (optionalCustomer.isPresent() && group != null) {
            Customer customer = optionalCustomer.get();
            customer.setGroup(null);
            if (customer.getGroupRole() == GroupRoles.GroupRole.USER) {
                customer.setGroupRole(null);
            } else {
                deleteGroup(groupId);
            }
            customerRepository.save(customer);
            return GroupMapper.INSTANCE.toDTO(group);
        }
        return null;
    }

    public void deleteGroup(long groupId) {
        groupRepository.deleteById(groupId);
    }
}

