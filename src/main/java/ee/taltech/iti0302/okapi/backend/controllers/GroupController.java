package ee.taltech.iti0302.okapi.backend.controllers;


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

    @GetMapping("/{id}")
    public GroupDTO getGroupById(@PathVariable long id) {
        return groupService.getGroupById(id);
    }

    @PutMapping("/create")
    public GroupDTO createGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.createGroup(groupDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable long id) {
        groupService.deleteGroup(id);
    }
}
