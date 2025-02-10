package org.app.unit;

import org.app.dto.todos.EditTaskRequestDto;
import org.app.dto.todos.TaskDto;
import org.app.dto.todos.TaskResponseDto;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.app.repository.todo.TodoRepository;
import org.app.service.TodoServiceImpl;
import org.app.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final Long id = 1L;
    private final String username = "test_user";

    @Test
    void testSaveTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Test task");
        taskDto.setComment("This is a test comment.");
        taskDto.setDueDate(LocalDate.now().plusDays(5));
        taskDto.setCompleted(false);

        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);

        TaskEntity savedEntity = new TaskEntity();
        savedEntity.setId(id);
        savedEntity.setTitle(taskDto.getTitle());
        savedEntity.setComment(taskDto.getComment());
        savedEntity.setDueDate(taskDto.getDueDate());
        savedEntity.setCompleted(taskDto.getCompleted());
        savedEntity.setCreatedDate(LocalDate.now());
        savedEntity.setUser(user);

        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(user));
        when(todoRepository.save(any(TaskEntity.class))).thenReturn(savedEntity);

        TaskResponseDto response = todoService.saveTask(taskDto, username);

        assertNotNull(response);
        assertEquals("Test task", response.getTitle());
        assertEquals("This is a test comment.", response.getComment());
        assertEquals(LocalDate.now().plusDays(5), response.getDueDate());
        assertEquals(id, response.getUserId());
        verify(todoRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void testGetTaskById() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);
        taskEntity.setTitle("Test task");
        taskEntity.setComment("This is a test comment.");
        taskEntity.setDueDate(LocalDate.now().plusDays(5));
        taskEntity.setCompleted(false);
        taskEntity.setUser(user);

        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(user));
        when(todoRepository.findById(id)).thenReturn(Optional.of(taskEntity));

        TaskResponseDto response = todoService.getTaskById(id, username);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Test task", response.getTitle());
        assertEquals(id, response.getUserId());
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    void testEditTask() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(id);
        existingTask.setTitle("Old title");
        existingTask.setComment("Old comment");
        existingTask.setDueDate(LocalDate.now().plusDays(5));
        existingTask.setCompleted(false);
        existingTask.setUser(user);

        EditTaskRequestDto taskDto = new EditTaskRequestDto();
        taskDto.setCompleted(true);

        TaskEntity updatedTask = new TaskEntity();
        updatedTask.setId(id);
        updatedTask.setTitle("New title");
        updatedTask.setComment("New comment");
        updatedTask.setDueDate(LocalDate.now().plusDays(5));
        updatedTask.setCompleted(true);
        updatedTask.setUser(user);

        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(user));
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTask));
        when(todoRepository.save(existingTask)).thenReturn(updatedTask);

        TaskResponseDto response = todoService.editTaskStatus(id, taskDto, username);

        assertNotNull(response);
        assertEquals("New title", response.getTitle());
        assertEquals("New comment", response.getComment());
        assertEquals(id, response.getUserId());
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(existingTask);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(new UserEntity()));
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> todoService.getTaskById(id, username));
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteTask() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);
        taskEntity.setUser(user);

        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(user));
        when(todoRepository.findById(id)).thenReturn(Optional.of(taskEntity));

        Long deletedId = todoService.deleteTask(id, username);

        verify(todoRepository, times(1)).deleteById(id);
        assertNotNull(deletedId);
        assertEquals(id, deletedId);
    }

    @Test
    void testDeleteTaskForbidden() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);
        taskEntity.setUser(anotherUser);

        when(userService.loadUserByUsernameOpt(username)).thenReturn(Optional.of(user));
        when(todoRepository.findById(id)).thenReturn(Optional.of(taskEntity));

        assertThrows(ResponseStatusException.class, () -> todoService.deleteTask(id, username));
        verify(todoRepository, times(0)).deleteById(id);
    }
}
