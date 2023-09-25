package ee.taltech.iti0302.okapi.backend.components;

import ee.taltech.iti0302.okapi.backend.dto.TasksDTO;
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

    public Object getTaskById(long id) {
        return tasksRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public Object createTask(TasksDTO taskDTO) {
        return tasksRepository.save(new Tasks(taskDTO.getTitle(), taskDTO.getDescription()));
    }

    public Object updateTask(long id, TasksDTO taskDTO) {
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

    public void deleteTask(long id) {
        tasksRepository.deleteById(id);
    }

    public Object convertToDTO(Tasks tasks) {
        return new TasksDTO(tasks.getId(), tasks.getTitle(), tasks.getDescription());
    }

    private Tasks convertToEntity(TasksDTO tasksDTO) {
        return new Tasks(tasksDTO.getTitle(), tasksDTO.getDescription());
    }

}
