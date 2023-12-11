package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.GroupMapper;
import ee.taltech.iti0302.okapi.backend.components.RecordsMapper;
import ee.taltech.iti0302.okapi.backend.dto.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Group;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TimerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public RecordsDTO getRecords() {
        Optional<Records> records = recordsRepository.findById(1L);
        return records.map(RecordsMapper.INSTANCE::toDTO).orElse(null);
    }

    public String getUsersNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records;
        records = optionalRecords.orElseGet(Records::new);
        return records.getNumberOfUsers().toString();
    }

    public String getTimersNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records;
        records = optionalRecords.orElseGet(Records::new);
        return records.getNumberOfTimers().toString();
    }

    public String getGroupsNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records;
        records = optionalRecords.orElseGet(Records::new);
        return records.getNumberOfGroups().toString();
    }

    public String getTasksNumber() {
        Optional<Records> optionalRecords = recordsRepository.findById(1L);
        Records records;
        records = optionalRecords.orElseGet(Records::new);
        return records.getNumberOfTasks().toString();
    }
}
