package org.app.dto.todos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String comment;
    private LocalDate createdDate;
    private LocalDate dueDate;
    private Boolean completed;
    private Long userId;
}
