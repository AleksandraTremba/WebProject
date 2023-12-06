package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.services.RecordsService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/records")
@CrossOrigin(origins = "http://127.0.0.1:5173")
@Validated
public class RecordsController {

    @NonNull
    private RecordsService recordsService;

    @GetMapping("/users")
    private String getUsersNumber() {
        return recordsService.getUsersNumber();
    }

    @GetMapping("/timers")
    private String getTimersNumber() {
        return recordsService.getTimersNumber();
    }

    @GetMapping("/groups")
    private String getGroupsNumber() {
        return recordsService.getGroupsNumber();
    }

    @GetMapping("/tasks")
    private String getTasksNumber() {
        return recordsService.getTasksNumber();
    }

}
