package org.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.app.dto.todos.*;
import org.app.entities.TaskEntity;
import org.app.entities.UserEntity;
import org.app.factories.ExceptionFactory;
import org.app.repository.todo.TodoRepository;
import org.app.repository.todo.TodoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log
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

    public Page<TaskResponseDto> searchEmployeeWithPaginationSortingAndFiltering(String username, TaskRequestDto taskRequestDto) {
        UserEntity loadedUser = userService.loadUserByUsernameOpt(username)
                .orElseThrow(ExceptionFactory::userNotFound);

        FilterDto filterDto = FilterDto.builder()
                .title(taskRequestDto.getTitle())
                .createdDate(taskRequestDto.getCreatedDate())
                .dueDate(taskRequestDto.getDueDate())
                .completed(taskRequestDto.getCompleted())
                .build();

        List<SortDto> sortDtos = jsonStringToSortDto(taskRequestDto.getSort());
        List<Sort.Order> orders = new ArrayList<>();

        if (sortDtos != null) {
            sortDtos.forEach(sortDto -> {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, sortDto.getField()));
            });
        }

        PageRequest pageRequest = PageRequest.of(
                taskRequestDto.getPage(),
                taskRequestDto.getSize(),
                Sort.by(orders)
        );

        Specification<TaskEntity> specification = TodoSpecification.getSpecification(loadedUser, filterDto);
        Page<TaskEntity> todos = todoRepository.findAll(specification, pageRequest);

        return todos.map(this::mapToResponseDto);
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

    private List<SortDto> jsonStringToSortDto(String jsonString) {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(jsonString, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.info(String.format("Exception: %s", e));
            return null;
        }
    }
}
