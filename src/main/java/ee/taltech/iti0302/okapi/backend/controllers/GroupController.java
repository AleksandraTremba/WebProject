package ee.taltech.iti0302.okapi.backend.controllers;


import ee.taltech.iti0302.okapi.backend.dto.customer.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.group.GroupDTO;
import ee.taltech.iti0302.okapi.backend.enums.GroupCustomerActionType;
import ee.taltech.iti0302.okapi.backend.services.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://127.0.0.1:5173")
public class GroupController {

    @NonNull
    private GroupService groupService;

    @GetMapping
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public GroupDTO getGroupById(@PathVariable long groupId) {
        return groupService.searchGroupById(groupId);
    }

    @PutMapping("/")
    public GroupDTO createGroup(@RequestParam @NotBlank String groupName, @RequestBody @Valid CustomerDTO customerDTO) {
        return groupService.manipulateCustomerAndGroup(customerDTO, groupName, GroupCustomerActionType.CREATE);
    }

    @PostMapping("/{groupName}/add")
    public GroupDTO addCustomerToGroup(@PathVariable @NotBlank String groupName, @RequestBody @Valid CustomerDTO customerDTO) {
        return groupService.manipulateCustomerAndGroup(customerDTO, groupName, GroupCustomerActionType.ADD);
    }

    @DeleteMapping("/{groupName}/remove_customer")
    public GroupDTO removeCustomerFromGroup(@PathVariable String groupName, @RequestBody CustomerDTO customerDTO) {
        return groupService.manipulateCustomerAndGroup(customerDTO, groupName, GroupCustomerActionType.DELETE);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable long groupId) {
        groupService.deleteGroup(groupId);
    }
}
