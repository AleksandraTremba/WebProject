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

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskDTO> getAllTasks() {
        log.debug("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .toList();
        log.debug("Retrieved {} tasks", taskDTOs.size());
        return taskDTOs;
    }

    public TaskDTO getTaskById(long id) {
        log.debug("Fetching task by ID: {}", id);
        Optional<Task> task = taskRepository.findById(id);
        TaskDTO taskDTO = task.map(TaskMapper.INSTANCE::toDTO).orElse(null);
        log.debug("Retrieved task: {}", taskDTO);
        return taskDTO;
    }

    public TaskDTO createTask(TaskDTO dto) {
        log.info("Creating task: {}", dto.getTitle());
        Task task = taskRepository.save(TaskMapper.INSTANCE.toEntity(dto));
        dto.setId(task.getId());
        if (task.getId() != null) {
            log.info("Task created successfully. Task ID: {}", task.getId());
            return dto;
        } else {
            log.warn("Task creation failed");
            return null;
        }
    }

    public TaskDTO updateTask(TaskDTO dto) {
        log.info("Updating task with ID: {}", dto.getId());
        Optional<Task> optionalTask = taskRepository.findById(dto.getId());
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(dto.getTitle());
            existingTask.setDescription(dto.getDescription());

            Task updatedTask = taskRepository.save(existingTask);
            log.info("Task updated successfully. Task ID: {}", updatedTask.getId());
            return TaskMapper.INSTANCE.toDTO(updatedTask);
        } else {
            log.warn("Task update failed. Task not found with ID: {}", dto.getId());
            return null;
        }
    }

    public void deleteTask(long id) {
        log.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        log.info("Task deleted successfully. Task ID: {}", id);
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
