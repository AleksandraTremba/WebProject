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
    private final TasksRepository tasksRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Object> getAllTasks() {
        List<Tasks> tasks = (List<Tasks>) tasksRepository.findAll();
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Object getTaskById(Long id) {
        Optional<Tasks> optionalTask = tasksRepository.findById(id);
        return optionalTask.map(this::convertToDTO).orElse(null);
    }

    public Object createTask(TasksDTO taskDTO) {
        Tasks task = convertToEntity(taskDTO);
        Tasks savedTask = tasksRepository.save(task);
        return convertToDTO(savedTask);
    }

    public Object updateTask(Long id, TasksDTO taskDTO) {
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

    public void deleteTask(Long id) {
        tasksRepository.deleteById(id);
    }

    public Object convertToDTO(Tasks tasks) {
        TasksDTO tasksDTO = new TasksDTO();
        tasksDTO.setId(tasks.getId());
        tasksDTO.setTitle(tasks.getTitle());
        tasksDTO.setDescription(tasks.getDescription());
        return tasksDTO;
    }

    private Tasks convertToEntity(TasksDTO tasksDTO) {
        Tasks task = new Tasks();
        task.setTitle(tasksDTO.getTitle());
        task.setDescription(task.getDescription());
        return task;
    }

}
