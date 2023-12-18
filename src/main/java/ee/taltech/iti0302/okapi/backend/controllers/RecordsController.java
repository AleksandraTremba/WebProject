package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.services.RecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/public/records")
public class RecordsController {
    private final RecordsService recordsService;

    @GetMapping("/")
    public RecordsDTO getAllRecords() {
        return recordsService.getRecords();
    }

    @GetMapping("/users")
    public String getUsersNumber() {
        return recordsService.getCustomersAmount();
    }

    @GetMapping("/timers")
    public String getTimersNumber() {
        return recordsService.getTimersNumber();
    }

    @GetMapping("/groups")
    public String getGroupsNumber() {
        return recordsService.getGroupsNumber();
    }

    @GetMapping("/tasks")
    public String getTasksNumber() {
        return recordsService.getTasksNumber();
    }

}
