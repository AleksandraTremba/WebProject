package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Records;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.repository.RecordsRepository;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final RecordsRepository recordsRepository;

    public List<TaskDTO> getAllTasks(int page) {
        Sort sort = Sort.by("status").descending();
        Pageable pageRequest = PageRequest.of(page, 10, sort);
        Page<Task> task = taskRepository.findAll(pageRequest);
        return task.getContent().stream()
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
        updateRecords();
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
        log.info(getCurrentTime() + ": " + "Task deleted successfully. Task ID: {}", id);
    }

    public List<TaskDTO> findByTitle(int page, String title) {
        Pageable paging = PageRequest.of(page, 10);
        Page<Task> tasks = taskRepository.findByTitle(title, paging);
        return tasks.getContent().stream()
                .map(TaskMapper.INSTANCE::toDTO)
                .toList();
    }
}
