package ee.taltech.iti0302.okapi.backend.controllers;


import ee.taltech.iti0302.okapi.backend.dto.CustomerDTO;
import ee.taltech.iti0302.okapi.backend.dto.GroupDTO;
import ee.taltech.iti0302.okapi.backend.services.GroupService;
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
        return groupService.getGroupById(groupId);
    }

    @PutMapping("/create")
    public GroupDTO createGroup(@RequestParam Long customerId) {
        return groupService.createGroup(customerId);
    }

    @PostMapping("/{groupId}/add")
    public GroupDTO addCustomerToGroup(@PathVariable long groupId, @RequestBody CustomerDTO customerDTO) {
        return groupService.addUserToGroup(customerDTO, groupId);
    }

    @DeleteMapping("/{groupId}/remove")
    public GroupDTO removeCustomerFromGroup(@PathVariable long groupId, @RequestBody CustomerDTO customerDTO) {
        return groupService.addUserToGroup(customerDTO, groupId);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable long groupId) {
        groupService.deleteGroup(groupId);
    }
}
