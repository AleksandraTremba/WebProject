package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.components.TasksService;
import ee.taltech.iti0302.okapi.backend.dto.TasksDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {
    private final TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping
    public List<Object> getAllTasks() {
        return tasksService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Object getTaskById(@PathVariable Long id) {
        return tasksService.getTaskById(id);
    }

    @PostMapping
    public Object createTask(@RequestBody TasksDTO taskDTO) {
        return tasksService.createTask(taskDTO);
    }

    @PutMapping("/{id}")
    public Object updateTask(@PathVariable Long id, @RequestBody TasksDTO taskDTO) {
        return tasksService.updateTask(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        tasksService.deleteTask(id);
    }
}