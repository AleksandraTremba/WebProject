package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class TaskService {
    @NonNull
    private TaskRepository taskRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    public List<TaskDTO> getAllTasks() {
        List<Task> task = (List<Task>) taskRepository.findAll();
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
            logger.info(task.getId().toString());
            return dto;
        }
        logger.log(Level.SEVERE, "Task was probably not created!");
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

    public TaskDTO updateTaskStatus(long id) {
        Optional<Task> optionalTasks = taskRepository.findById(id);
        if (optionalTasks.isPresent()) {
            Task existingTask = optionalTasks.get();
            TaskStatus status = existingTask.getStatus();
            if (status == TaskStatus.TODO) {
                existingTask.setStatus(TaskStatus.WORKING);
            } else if (status == TaskStatus.WORKING) {
                existingTask.setStatus(TaskStatus.DONE);
            } else if (status == TaskStatus.DONE) {
                existingTask.setStatus(TaskStatus.TODO);
            }
            taskRepository.save(existingTask);
            return TaskMapper.INSTANCE.toDTO(existingTask);
        }
        return null;
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }
}
