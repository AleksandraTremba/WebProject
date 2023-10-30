package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import ee.taltech.iti0302.okapi.backend.components.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskDTO> getAllTasks() {
        List<Task> task = (List<Task>) taskRepository.findAll();
        return task.stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(TaskMapper.INSTANCE::toDTO).orElse(null);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.INSTANCE.toEntity(taskDTO);
        task = taskRepository.save(task);
        return TaskMapper.INSTANCE.toDTO(task);
    }

    public TaskDTO updateTask(long id, TaskDTO task) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());

            Task updatedTask = taskRepository.save(existingTask);
            return TaskMapper.INSTANCE.toDTO(updatedTask);
        }
        return null;
    }

    public TaskDTO statusTask(long id) {
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
