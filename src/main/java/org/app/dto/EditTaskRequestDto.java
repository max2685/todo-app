package org.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditTaskRequestDto {
        @NotNull(message = "Completion status must not be null")
        private Boolean completed;
}
