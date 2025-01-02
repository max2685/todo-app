package org.app.service;

import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;

public interface TodoService {
    TaskResponseDto saveTask(TaskDto taskDto);
    TaskResponseDto getTaskById(Long id);
    TaskResponseDto editTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
}
