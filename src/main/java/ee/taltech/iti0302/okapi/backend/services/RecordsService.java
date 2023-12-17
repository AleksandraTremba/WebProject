package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.RecordsMapper;
import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.enums.CustomerServiceUpdate;
import ee.taltech.iti0302.okapi.backend.enums.RecordType;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordsService {
    private final RecordsRepository recordsRepository;

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public RecordsDTO getRecords() {
        Optional<Records> records = recordsRepository.findById(1L);
        RecordsDTO recordsDTO = records.map(RecordsMapper.INSTANCE::toDTO).orElse(null);
        log.info(getCurrentTime() + ": " + "Retrieved records: {}", recordsDTO);
        return recordsDTO;
    }

    public String getCustomersAmount() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfCustomers = records.getNumberOfCustomers().toString();
        log.info(getCurrentTime() + ": " + "Fetched total amount of customers: {}", numberOfCustomers);
        return numberOfCustomers;
    }

    public String getTimersNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfTimers = records.getNumberOfTimers().toString();
        log.info(getCurrentTime() + ": " + "Fetched total amount of timers: {}", numberOfTimers);
        return numberOfTimers;
    }

    public String getGroupsNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfGroups = records.getNumberOfGroups().toString();
        log.info(getCurrentTime() + ": " + "Fetched total amount of groups: {}", numberOfGroups);
        return numberOfGroups;
    }

    public String getTasksNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        String numberOfTasks = records.getNumberOfTasks().toString();
        log.info(getCurrentTime() + ": " + "Fetched total amount of tasks: {}", numberOfTasks);
        return numberOfTasks;
    }

    public void updateRecords(RecordType updateType) {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records = optionalRecords.orElseGet(Records::new);
        if (updateType.equals(RecordType.CUSTOMERS)) {
            records.setNumberOfCustomers(records.getNumberOfCustomers() + 1);
        }
        if (updateType.equals(RecordType.TIMERS)) {
            records.setNumberOfTimers(records.getNumberOfTimers() + 1);
        }
        if (updateType.equals(RecordType.TASKS)) {
            records.setNumberOfTasks(records.getNumberOfTasks() + 1);
        }
        if (updateType.equals(RecordType.GROUPS)) {
            records.setNumberOfGroups(records.getNumberOfGroups() + 1);
        }
    }
}
