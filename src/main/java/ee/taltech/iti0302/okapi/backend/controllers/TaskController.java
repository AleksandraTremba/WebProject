package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.TaskDTO;
import ee.taltech.iti0302.okapi.backend.services.TaskService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    @NonNull
    private TaskService taskService;

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/create")
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @PostMapping("/update")
    public TaskDTO updateTask(@RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO);
    }

    @PatchMapping("/{id}/status")
    public TaskDTO updateTaskStatus(@PathVariable long id) {
        return taskService.updateTaskStatus(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}