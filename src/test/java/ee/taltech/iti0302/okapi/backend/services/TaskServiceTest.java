package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.components.TaskMapper;
import ee.taltech.iti0302.okapi.backend.dto.task.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Task;
import ee.taltech.iti0302.okapi.backend.enums.TaskStatus;
import ee.taltech.iti0302.okapi.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    private TaskDTO buildTaskDTO(Long id, Long customerId, String title, String description, String status) {
        return TaskDTO.builder()
                .id(id)
                .customerId(customerId)
                .title(title)
                .description(description)
                .status(status)
                .build();
    }

    private Task buildTask(Long customerId, String title, String description, String status) {
        return Task.builder()
                .customerId(customerId)
                .title(title)
                .description(description)
                .status(TaskStatus.valueOf(status))
                .build();
    }


    @Test
    public void testGetTaskById() {
        Task task = buildTask(1L, "Test Task", null, "TODO");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskDTO taskDTO = taskService.getTaskById(1L);

        assertNull(taskDTO.getDescription());
        assertEquals("Test Task", taskDTO.getTitle());

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testGetTaskByIdNotFound() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskDTO taskDTO = taskService.getTaskById(1L);

        assertNull(taskDTO);

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testCreateTask() {
        TaskDTO taskDTO = buildTaskDTO( 1L, 1L, "Test Task", "description", "TODO");

        Task task = buildTask(1L, "Task Title", null, "TODO");

        when(TaskMapper.INSTANCE.toEntity(taskDTO)).thenReturn(null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO resultDTO = taskService.createTask(taskDTO);

        assertNotNull(resultDTO);
        assertEquals(1L, resultDTO.getId());
    }

    @Test
    public void testCreateTaskFailure() {
        TaskDTO taskDTO = buildTaskDTO(1L, 2L, "Title", null, "TODO");

        when(taskRepository.save(any(Task.class))).thenReturn(null);

        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        assertNull(createdTaskDTO);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        TaskDTO taskDTO = buildTaskDTO(1L, 1L, "Updated Title", "Updated Description", "TODO");
        Task existingTask = buildTask(1L, "Old Title", "Old Description", "TODO");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(existingTask);

        TaskDTO updatedTaskDTO = taskService.updateTask(taskDTO);

        assertNull(updatedTaskDTO.getId());
        assertEquals("Updated Title", updatedTaskDTO.getTitle());
        assertEquals("Updated Description", updatedTaskDTO.getDescription());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    public void testUpdateTaskNotFound() {
        TaskDTO taskDTO = buildTaskDTO(1L, 1L, "Updated Title", "Updated Description", "TODO");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskDTO updatedTaskDTO = taskService.updateTask(taskDTO);

        assertNull(updatedTaskDTO);

        verify(taskRepository).findById(taskDTO.getId());

        verify(taskRepository, Mockito.never()).save(Mockito.any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        TaskDTO taskDTO = buildTaskDTO(1L, 1L, "Title", null, "TODO");

        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(taskDTO.getId());

        verify(taskRepository).existsById(taskDTO.getId());
        verify(taskRepository).deleteById(taskDTO.getId());
    }

    @Test
    public void testDeleteTaskNotFound() {
        TaskDTO taskDTO = buildTaskDTO(1L, 1L, "Title", null, "TODO");

        when(taskRepository.existsById(anyLong())).thenReturn(false);

        taskService.deleteTask(taskDTO.getId());

        verify(taskRepository).existsById(taskDTO.getId());

        verify(taskRepository, Mockito.never()).deleteById(taskDTO.getId());
    }

    @Test
    public void testFindByTitle() {
        Task task1 = buildTask(1L, "Test Title", "Title", "TODO");
        Task task2 = buildTask(2L, "Test Title", "Title", "TODO");

        int page = 0;
        List<Task> taskList = List.of(task1, task2);
        Page<Task> taskPage = new PageImpl<>(taskList);

        when(taskRepository.findByTitle(any(String.class), any(Pageable.class))).thenReturn(taskPage);

        List<TaskDTO> result = taskService.findByTitle(page, "Test Title");

        assertEquals(2, result.size());

        verify(taskRepository).findByTitle(eq("Test Title"), any(Pageable.class));
    }
}
