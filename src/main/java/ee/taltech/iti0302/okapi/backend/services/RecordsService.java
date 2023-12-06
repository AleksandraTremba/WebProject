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
        if (recordsRepository.findById(1L).isPresent()) {
            Optional<Records> optionalRecords = recordsRepository.findById(1L);
            Records records = optionalRecords.get();
            return String.valueOf(records.getNumberOfUsers());
        } else {
            Records records = new Records();
            return String.valueOf(records.getNumberOfUsers());
        }
    }

    public String getTimersNumber() {
        if (recordsRepository.findById(1L).isPresent()) {
            Optional<Records> optionalRecords = recordsRepository.findById(1L);
            Records records = optionalRecords.get();
            return String.valueOf(records.getNumberOfTimers());
        } else {
            Records records = new Records();
            return String.valueOf(records.getNumberOfTimers());
        }
    }

    public String getGroupsNumber() {
        if (recordsRepository.findById(1L).isPresent()) {
            Optional<Records> optionalRecords = recordsRepository.findById(1L);
            Records records = optionalRecords.get();
            return String.valueOf(records.getNumberOfGroups());
        } else {
            Records records = new Records();
            return String.valueOf(records.getNumberOfGroups());
        }
    }

    public String getTasksNumber() {
        if (recordsRepository.findById(1L).isPresent()) {
            Optional<Records> optionalRecords = recordsRepository.findById(1L);
            Records records = optionalRecords.get();
            return String.valueOf(records.getNumberOfTasks());
        } else {
            Records records = new Records();
            return String.valueOf(records.getNumberOfTasks());
        }
    }
}
