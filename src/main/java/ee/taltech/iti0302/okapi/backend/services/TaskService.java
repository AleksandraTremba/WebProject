package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.task.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .toList();
        log.info(getCurrentTime() + ": " + "Retrieved {} tasks", taskDTOs.size());
        return taskDTOs;
    }

    public TaskDTO getTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        TaskDTO taskDTO = task.map(TaskMapper.INSTANCE::toDTO).orElse(null);
        log.info(getCurrentTime() + ": " + "Retrieved task by ID: {}", id);
        return taskDTO;
    }

    public TaskDTO createTask(TaskDTO dto) {
        log.info(getCurrentTime() + ": " + "Creating task: {}", dto.getTitle());
        Task task = taskRepository.save(TaskMapper.INSTANCE.toEntity(dto));
        dto.setId(task.getId());
        if (task.getId() != null) {
            log.info(getCurrentTime() + ": " + "Task created successfully. Task ID: {}", task.getId());
            return dto;
        } else {
            log.warn(getCurrentTime() + ": " + "Task creation failed");
            return null;
        }
    }

    public TaskDTO updateTask(TaskDTO dto) {
        log.info(getCurrentTime() + ": " + "Updating task with ID: {}", dto.getId());
        Optional<Task> optionalTask = taskRepository.findById(dto.getId());
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(dto.getTitle());
            existingTask.setDescription(dto.getDescription());

            Task updatedTask = taskRepository.save(existingTask);
            log.info(getCurrentTime() + ": " + "Task updated successfully. Task ID: {}", updatedTask.getId());
            return TaskMapper.INSTANCE.toDTO(updatedTask);
        } else {
            log.warn(getCurrentTime() + ": " + "Task update failed. Task not found with ID: {}", dto.getId());
            return null;
        }
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
        log.info(getCurrentTime() + ": " + "Task deleted successfully. Task ID: {}", id);
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
