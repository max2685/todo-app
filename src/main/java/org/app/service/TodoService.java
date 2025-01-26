package org.app.service;

import org.app.dto.EditTaskRequestDto;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;

import java.util.List;

public interface TodoService {
    TaskResponseDto saveTask(TaskDto taskDto, String username);

    List<TaskResponseDto> getAllTasks(String username);

    TaskResponseDto getTaskById(Long id, String username);

    TaskResponseDto editTaskStatus(Long id, EditTaskRequestDto taskDto, String username);

    Long deleteTask(Long id, String username);

    List<TaskResponseDto> filterTasks(String username, String createdDate, String dueDate, boolean completed, String query);
}