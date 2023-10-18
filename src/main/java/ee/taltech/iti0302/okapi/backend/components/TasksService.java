package ee.taltech.iti0302.okapi.backend.components;

import ee.taltech.iti0302.okapi.backend.dto.TasksDTO;
import ee.taltech.iti0302.okapi.backend.entities.TaskStatus;
import ee.taltech.iti0302.okapi.backend.entities.Tasks;
import ee.taltech.iti0302.okapi.backend.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TasksService {
    @Autowired
    private TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Object> getAllTasks() {
        List<Tasks> tasks = (List<Tasks>) tasksRepository.findAll();
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TasksDTO getTaskById(long id) {
        Optional<Tasks> task = tasksRepository.findById(id);
        return task.map(this::convertToDTO).orElse(null);
    }

    public void createTask(TasksDTO taskDTO) {
        tasksRepository.save(convertToEntity(taskDTO));
    }

    public TasksDTO updateTask(long id, TasksDTO taskDTO) {
        Optional<Tasks> optionalTask = tasksRepository.findById(id);
        if (optionalTask.isPresent()) {
            Tasks existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());

            Tasks updatedTask = tasksRepository.save(existingTask);
            return convertToDTO(updatedTask);
        }
        return null;
    }

    public TasksDTO statusTask(long id, TasksDTO taskDTO) {
        Optional<Tasks> optionalTasks = tasksRepository.findById(id);
        if (optionalTasks.isPresent()) {
            Tasks existingTask = optionalTasks.get();
            TaskStatus status = existingTask.getStatus();
            if (status == TaskStatus.TODO) {
                existingTask.setStatus(TaskStatus.WORKING);
            } else if (status == TaskStatus.WORKING) {
                existingTask.setStatus(TaskStatus.DONE);
            } else if (status == TaskStatus.DONE) {
                existingTask.setStatus(TaskStatus.TODO);
            }
            tasksRepository.save(existingTask);
            return convertToDTO(existingTask);
        }
        return null;
    }

    public void deleteTask(long id) {
        tasksRepository.deleteById(id);
    }

    public TasksDTO convertToDTO(Tasks tasks) {
        return new TasksDTO(tasks.getId(), tasks.getTitle(), tasks.getDescription(), tasks.getStatus());
    }

    private Tasks convertToEntity(TasksDTO tasksDTO) {
        return new Tasks(tasksDTO.getTitle(), tasksDTO.getDescription(), tasksDTO.getStatus());
    }

}
