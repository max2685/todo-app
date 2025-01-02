package org.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponseDto {
    private Long id;
    private String title;
    private String comment;
    private LocalDate createdDate;
    private LocalDate dueDate;
    private boolean completed;
    private Long userId;
}
