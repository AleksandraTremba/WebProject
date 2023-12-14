package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.RecordsMapper;
import ee.taltech.iti0302.okapi.backend.dto.records.RecordsDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
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
