package org.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.app.dto.EditTaskRequestDto;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.service.TodoServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/user/todos")
public class TodoController {
    private final TodoServiceImpl todoServiceImpl;

    @PostMapping
    public ResponseEntity<TaskResponseDto> saveTask(@Valid @RequestBody TaskDto taskCreateDto, Authentication authentication) {
        TaskResponseDto createdTask = todoServiceImpl.saveTask(taskCreateDto, authentication.getName());
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") Long id, Authentication authentication) {
        TaskResponseDto task = todoServiceImpl.getTaskById(id, authentication.getName());
        return ResponseEntity.ok(task);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(Authentication authentication) {
        List<TaskResponseDto> tasks = todoServiceImpl.getAllTasks(authentication.getName());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> filterTasks(
            @RequestParam(value = "createdDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") String createdDate,

            @RequestParam(value = "dueDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") String dueDate,

            @RequestParam(value = "completed", required = false) boolean completed,
            @RequestParam(value = "title", required = false) String title,
            Authentication authentication) {

        List<TaskResponseDto> tasks = todoServiceImpl.filterTasks(authentication.getName(), createdDate, dueDate, completed, title);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable("id") Long id, @Valid @RequestBody EditTaskRequestDto taskEditDto, Authentication authentication) {
        TaskResponseDto updatedTask = todoServiceImpl.editTaskStatus(id, taskEditDto, authentication.getName());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id, Authentication authentication) {
        Long deletedId = todoServiceImpl.deleteTask(id, authentication.getName());
        return ResponseEntity.ok().body("Deleted task id: " + deletedId);
    }
}
