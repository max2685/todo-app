package org.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.app.dto.TaskDto;
import org.app.dto.TaskResponseDto;
import org.app.service.TodoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/user")
public class TodoController { // todo

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

    @PutMapping("/{id}") // todo
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskDto taskEditDto, Authentication authentication) {
        TaskResponseDto updatedTask = todoServiceImpl.editTask(id, taskEditDto, authentication.getName());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id, Authentication authentication) {
        Long deletedId = todoServiceImpl.deleteTask(id, authentication.getName());
        return ResponseEntity.ok().body("Deleted task id: " + deletedId);
    }
}
