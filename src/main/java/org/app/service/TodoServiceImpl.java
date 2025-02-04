package org.app.service;

import lombok.RequiredArgsConstructor;
import org.app.dto.EditTaskRequestDto;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.app.factories.ExceptionFactory;
import org.app.repository.TodoRepository;
import org.springframework.stereotype.Service;

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
                .orElseThrow(ExceptionFactory::userNotFound);

        validateDates(taskCreateDto);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskCreateDto.getTitle());
        taskEntity.setComment(taskCreateDto.getComment());
        taskEntity.setDueDate(taskCreateDto.getDueDate());
        taskEntity.setCompleted(taskCreateDto.getCompleted());
        taskEntity.setCreatedDate(LocalDate.now());
        taskEntity.setUser(loadedUser);

        TaskEntity savedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(ExceptionFactory::userNotFound);

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(ExceptionFactory::taskNotFound);

        if (!taskEntity.getUser().equals(loadedUser)) throw ExceptionFactory.unauthorizedTaskAction();

        return mapToResponseDto(taskEntity);
    }

    @Override
    public List<TaskResponseDto> filterTasks(String username, LocalDate createdDate, LocalDate dueDate, Boolean completed, String title) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(ExceptionFactory::userNotFound);

        List<TaskEntity> tasks = todoRepository.filterTasks(loadedUser, createdDate, dueDate, completed, title);
        return tasks.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto editTaskStatus(Long id, EditTaskRequestDto taskEditDto, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(ExceptionFactory::userNotFound);

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(ExceptionFactory::taskNotFound);

        if (!taskEntity.getUser().equals(loadedUser)) throw ExceptionFactory.unauthorizedTaskAction();

        taskEntity.setCompleted(taskEditDto.getCompleted());

        TaskEntity updatedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(updatedTask);
    }

    @Override
    public Long deleteTask(Long id, String username) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(ExceptionFactory::userNotFound);

        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(ExceptionFactory::taskNotFound);

        if (!taskEntity.getUser().equals(loadedUser)) throw ExceptionFactory.unauthorizedTaskAction();

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
                .completed(taskEntity.getCompleted())
                .userId(taskEntity.getUser().getId())
                .build();
    }

    private void validateDates(TaskDto taskCreateDto) {
        if (taskCreateDto.getDueDate() != null) {
            if (taskCreateDto.getDueDate().isBefore(LocalDate.now())) {
                throw ExceptionFactory.dueDateNotCorrect();
            }
        }
    }
}
