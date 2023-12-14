package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.task.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskDTO> getAllTasks() {
        List<Task> task = taskRepository.findAll();
        return task.stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .toList();
    }

    public TaskDTO getTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(TaskMapper.INSTANCE::toDTO).orElse(null);
    }

    public TaskDTO createTask(TaskDTO dto) {
        Task task = taskRepository.save(TaskMapper.INSTANCE.toEntity(dto));
        dto.setId(task.getId());
        if (task.getId() != null) {
            return dto;
        }
        return null;
    }

    public TaskDTO updateTask(TaskDTO dto) {
        Optional<Task> optionalTask = taskRepository.findById(dto.getId());
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(dto.getTitle());
            existingTask.setDescription(dto.getDescription());

            Task updatedTask = taskRepository.save(existingTask);
            return TaskMapper.INSTANCE.toDTO(updatedTask);
        }
        return null;
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

//    private void updateRecords() {
//        Records records = recordsRepository.findById(1L).orElseGet(() -> {
//            Records newRecords = new Records();
//            recordsRepository.save(newRecords);
//            return newRecords;
//        });
//
//        records.setNumberOfTasks(records.getNumberOfTasks() + 1);
//        recordsRepository.save(records);
//    }
}
