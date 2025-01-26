package org.app.service;

import lombok.RequiredArgsConstructor;
import org.app.dto.EditTaskRequestDto;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.app.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final UserService userService;

    @Override
    public TaskResponseDto saveTask(TaskDto taskCreateDto, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        validateDates(taskCreateDto);

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this task");
        }

        return mapToResponseDto(taskEntity);
    }

    @Override
    public List<TaskResponseDto> getAllTasks(String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<TaskEntity> tasks = todoRepository.findAllByUser(loadedUser);
        return tasks.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> filterTasks(String username, String createdDate, String dueDate, boolean completed, String title) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDate parsedCreatedDate = parseDate(createdDate);
        LocalDate parsedDueDate = parseDate(dueDate);

        List<TaskEntity> tasks = todoRepository.filterTasks(loadedUser, parsedCreatedDate, parsedDueDate, completed, title);
        return tasks.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto editTaskStatus(Long id, EditTaskRequestDto taskEditDto, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this task");
        }
        taskEntity.setCompleted(taskEditDto.isCompleted());

        TaskEntity updatedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(updatedTask);
    }

    @Override
    public Long deleteTask(Long id, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!taskEntity.getUser().equals(loadedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this task");
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

    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        } else {
            return LocalDate.parse(date);
        }
    }

    private void validateDates(TaskDto taskCreateDto) {
        if (taskCreateDto.getDueDate() != null) {
            if (taskCreateDto.getDueDate().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Due date cannot be in the past");
            }
        }
    }
}
