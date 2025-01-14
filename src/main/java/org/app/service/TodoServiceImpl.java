package org.app.service;

import lombok.RequiredArgsConstructor;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.app.repository.TodoRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final UserServiceImpl userService;

    @Override
    public TaskResponseDto saveTask(TaskDto taskCreateDto, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskCreateDto.getTitle());
        taskEntity.setComment(taskCreateDto.getComment());
        taskEntity.setDueDate(taskCreateDto.getDueDate());
        taskEntity.setCompleted(taskCreateDto.isCompleted());
        taskEntity.setCreatedDate(LocalDate.now());
        taskEntity.setUser(loadedUser);

        TaskEntity savedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new SecurityException("You do not have permission to access this task");
        }

        return mapToResponseDto(taskEntity);
    }

    @Override
    public TaskResponseDto editTask(Long id, TaskDto taskEditDto, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new SecurityException("You do not have permission to update this task");
        }

        taskEntity.setTitle(taskEditDto.getTitle());
        taskEntity.setComment(taskEditDto.getComment());
        taskEntity.setDueDate(taskEditDto.getDueDate());
        taskEntity.setCompleted(taskEditDto.isCompleted());

        TaskEntity updatedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(updatedTask);
    }

    @Override
    public Long deleteTask(Long id, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new SecurityException("You do not have permission to delete this task");
        }

        todoRepository.deleteById(id);
        return id;
    }

    private TaskResponseDto mapToResponseDto(TaskEntity taskEntity) {
        return TaskResponseDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .comment(taskEntity.getComment())
                .createdDate(taskEntity.getCreatedDate())
                .dueDate(taskEntity.getDueDate())
                .completed(taskEntity.isCompleted())
                .userId(taskEntity.getUser().getId())
                .build();
    }
}
