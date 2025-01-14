package org.app.service;

import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;

public interface TodoService {
    TaskResponseDto saveTask(TaskDto taskDto, String username);
    TaskResponseDto getTaskById(Long id, String username);
    TaskResponseDto editTask(Long id, TaskDto taskDto, String username);
    Long deleteTask(Long id, String username);
}
