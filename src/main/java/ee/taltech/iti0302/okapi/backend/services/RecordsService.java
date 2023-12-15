package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.RecordsMapper;
import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public RecordsDTO getRecords() {
        log.debug("Fetching records");
        Optional<Records> records = recordsRepository.findById(1L);
        RecordsDTO recordsDTO = records.map(RecordsMapper.INSTANCE::toDTO).orElse(null);
        log.debug("Retrieved records: {}", recordsDTO);
        return recordsDTO;
    }

    public String getUsersNumber() {
        log.debug("Fetching number of users");
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfUsers = records.getNumberOfUsers().toString();
        log.debug("Number of users: {}", numberOfUsers);
        return numberOfUsers;
    }

    public String getTimersNumber() {
        log.debug("Fetching number of timers");
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfTimers = records.getNumberOfTimers().toString();
        log.debug("Number of timers: {}", numberOfTimers);
        return numberOfTimers;
    }

    public String getGroupsNumber() {
        log.debug("Fetching number of groups");
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfGroups = records.getNumberOfGroups().toString();
        log.debug("Number of groups: {}", numberOfGroups);
        return numberOfGroups;
    }

    public String getTasksNumber() {
        log.debug("Fetching number of tasks");
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfTasks = records.getNumberOfTasks().toString();
        log.debug("Number of tasks: {}", numberOfTasks);
        return numberOfTasks;
    }
}
