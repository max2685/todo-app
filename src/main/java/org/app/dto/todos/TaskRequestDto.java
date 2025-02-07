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
public class TaskRequestDto {
    private Long id;
    private String title;
    private LocalDate createdDate;
    private LocalDate dueDate;
    private Boolean completed;
    private Integer page;
    private Integer size;
    private String sort;
}