package org.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TaskDto {
    @NotNull(message = "Title must not be null")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @NotNull(message = "Completion status must not be null")
    private Boolean completed;
}
