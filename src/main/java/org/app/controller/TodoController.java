package org.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.app.dto.todos.EditTaskRequestDto;
import org.app.dto.todos.TaskDto;
import org.app.dto.todos.TaskRequestDto;
import org.app.dto.todos.TaskResponseDto;
import org.app.service.TodoServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Log
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

    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> filterTasks(
            @RequestParam(value = "createdDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,

            @RequestParam(value = "dueDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,

            @RequestParam(value = "completed", required = false) Boolean completed,
            @RequestParam(value = "title", required = false) String title,
            Authentication authentication) {

        List<TaskResponseDto> tasks = todoServiceImpl.filterTasks(authentication.getName(), createdDate, dueDate, completed, title);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filtering-pagination-sorting")
    public ResponseEntity<Page<TaskResponseDto>> searchEmployeeWithPaginationSortingAndFiltering(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"createdDate\",\"direction\":\"desc\"}]", required = false) String sort,

            @RequestParam(value = "createdDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,

            @RequestParam(value = "dueDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,

            @RequestParam(value = "completed", required = false) Boolean completed,
            @RequestParam(value = "title", required = false) String title,
            Authentication authentication) {

        Page<TaskResponseDto> taskResponseDtoPage = todoServiceImpl.searchEmployeeWithPaginationSortingAndFiltering(
                authentication.getName(),
                TaskRequestDto.builder()
                        .createdDate(createdDate)
                        .dueDate(dueDate)
                        .completed(completed)
                        .title(title)
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());

        return ResponseEntity.ok(taskResponseDtoPage);
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
