package org.app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthRequestDto {

    @Email
    @NotBlank
    @NotNull(message = "Username must not be null")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;
}
