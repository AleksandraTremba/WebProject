package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.GroupDTO;
import ee.taltech.iti0302.okapi.backend.entities.Customer;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.repository.CustomerRepository;
import ee.taltech.iti0302.okapi.backend.repository.GroupRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {
    @NonNull
    private GroupRepository groupRepository;

    @NonNull
    private CustomerRepository customerRepository;

    public GroupDTO createGroup(GroupDTO dto) {
        Group group = groupRepository.save(GroupMapper.INSTANCE.toEntity(dto));
        dto.setId(group.getId());
        if (group.getId() != null) {
            return dto;
        }
        return null;
    }

    public GroupDTO getGroupById(long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        return group.map(GroupMapper.INSTANCE::toDTO).orElse(null);
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
            customerRepository.save(customer);
            return GroupMapper.INSTANCE.toDTO(group);
        }
        return null;
    }

    public void deleteGroup(long groupId) {
        groupRepository.deleteById(groupId);
    }
}

