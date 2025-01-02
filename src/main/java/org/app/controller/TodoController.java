package org.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.service.TodoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api")
public class TodoController {
    private final TodoServiceImpl todoServiceImpl;

    @PostMapping
    public ResponseEntity<TaskResponseDto> saveTask(@Valid @RequestBody TaskDto taskCreateDto) {
        TaskResponseDto createdTask = todoServiceImpl.saveTask(taskCreateDto);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("id") Long id) {
        TaskResponseDto task = todoServiceImpl.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskDto taskEditDto) {
        TaskResponseDto updatedTask = todoServiceImpl.editTask(id, taskEditDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        todoServiceImpl.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
