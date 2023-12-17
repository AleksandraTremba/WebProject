package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.dto.task.TaskDTO;
import ee.taltech.iti0302.okapi.backend.entities.Task;
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


    private TaskDTO buildTaskDTO(String title, String description) {
        return TaskDTO.builder()
                .id(1L)
                .customerId(1L)
                .title(title)
                .description(description)
                .build();
    }

    private Task buildTask() {
        return new Task();
    }


    @Test
    public void testGetTaskById() {
        Task task = buildTask();

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskDTO taskDTO = taskService.getTaskById(1L);

        assertNull(taskDTO.getDescription());
        assertNull(taskDTO.getTitle());

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
    public void testCreateTask_Success() {
        TaskDTO taskDTO = buildTaskDTO("Title", null);
        Task savedTask = new Task();
        savedTask.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(savedTask.getId(), result.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testCreateTask_Failure() {
        TaskDTO taskDTO = buildTaskDTO("Title", null);

        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        TaskDTO result = taskService.createTask(taskDTO);

        assertNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        TaskDTO taskDTO = buildTaskDTO("Updated Title", "Updated Description");
        Task existingTask = buildTask();

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
        TaskDTO taskDTO = buildTaskDTO("Updated Title", "Updated Description");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskDTO updatedTaskDTO = taskService.updateTask(taskDTO);

        assertNull(updatedTaskDTO);

        verify(taskRepository).findById(taskDTO.getId());

        verify(taskRepository, Mockito.never()).save(Mockito.any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        TaskDTO taskDTO = buildTaskDTO("Title", null);

        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(taskDTO.getId());

        verify(taskRepository).existsById(taskDTO.getId());
        verify(taskRepository).deleteById(taskDTO.getId());
    }

    @Test
    public void testDeleteTaskNotFound() {
        TaskDTO taskDTO = buildTaskDTO("Title", null);

        when(taskRepository.existsById(anyLong())).thenReturn(false);

        taskService.deleteTask(taskDTO.getId());

        verify(taskRepository).existsById(taskDTO.getId());

        verify(taskRepository, Mockito.never()).deleteById(taskDTO.getId());
    }

    @Test
    public void testFindByTitle() {
        Task task1 = buildTask();
        Task task2 = buildTask();

        int page = 0;
        List<Task> taskList = List.of(task1, task2);
        Page<Task> taskPage = new PageImpl<>(taskList);

        when(taskRepository.findByTitle(any(String.class), any(Pageable.class))).thenReturn(taskPage);

        List<TaskDTO> result = taskService.findByTitle(page, "Test Title");

        assertEquals(2, result.size());

        verify(taskRepository).findByTitle(eq("Test Title"), any(Pageable.class));
    }
}
