package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.components.TasksService;
import ee.taltech.iti0302.okapi.backend.dto.TasksDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TasksController {
    @Autowired
    private TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping
    public List<Object> getAllTasks() {
        return tasksService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TasksDTO getTaskById(@PathVariable long id) {
        return tasksService.getTaskById(id);
    }

    @PostMapping
    public void createTask(@RequestBody TasksDTO taskDTO) {
        tasksService.createTask(taskDTO);
    }

    @PutMapping("/{id}")
    public TasksDTO updateTask(@PathVariable long id, @RequestBody TasksDTO taskDTO) {
        return tasksService.updateTask(id, taskDTO);
    }

    @PatchMapping("/{id}/status")
    public TasksDTO updateTaskStatus(@PathVariable long id, @RequestBody TasksDTO taskDTO) {
        return tasksService.statusTask(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        tasksService.deleteTask(id);
    }
}