package org.app.service;

import lombok.RequiredArgsConstructor;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.entities.TaskEntity;
import org.app.repository.TodoRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Override
    public TaskResponseDto saveTask(TaskDto taskCreateDto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskCreateDto.getTitle());
        taskEntity.setComment(taskCreateDto.getComment());
        taskEntity.setDueDate(taskCreateDto.getDueDate());
        taskEntity.setCompleted(taskCreateDto.isCompleted());
        taskEntity.setCreatedDate(LocalDate.now());

        TaskEntity savedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("Task not found"));
        return mapToResponseDto(taskEntity);
    }

    @Override
    public TaskResponseDto editTask(Long id, TaskDto taskEditDto) {
        TaskEntity taskEntity = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("Task not found"));

        taskEntity.setTitle(taskEditDto.getTitle());
        taskEntity.setComment(taskEditDto.getComment());
        taskEntity.setDueDate(taskEditDto.getDueDate());
        taskEntity.setCompleted(taskEditDto.isCompleted());

        TaskEntity updatedTask = todoRepository.save(taskEntity);
        return mapToResponseDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        todoRepository.deleteById(id);
    }

    private TaskResponseDto mapToResponseDto(TaskEntity taskEntity) {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskEntity.getId());
        responseDto.setTitle(taskEntity.getTitle());
        responseDto.setComment(taskEntity.getComment());
        responseDto.setCreatedDate(taskEntity.getCreatedDate());
        responseDto.setDueDate(taskEntity.getDueDate());
        responseDto.setCompleted(taskEntity.isCompleted());
        responseDto.setUserId(taskEntity.getUser().getId());
        return responseDto;
    }
}
