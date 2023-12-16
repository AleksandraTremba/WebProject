package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.dto.task.TaskDTO;
import ee.taltech.iti0302.okapi.backend.services.TaskService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @NonNull
    private TaskService taskService;

    @GetMapping
    public List<TaskDTO> getAllTasks(@RequestParam(defaultValue = "0") int page) {
        return taskService.getAllTasks(page);
    }

    @GetMapping("/title")
    public List<TaskDTO> findByTitle(@RequestParam(defaultValue = "0") int page, @RequestParam() String title) {
        return taskService.findByTitle(page, title);
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

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}