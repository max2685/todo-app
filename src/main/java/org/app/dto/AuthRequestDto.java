package org.app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthRequestDto {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Username is required")
    @NotNull(message = "Username must not be null")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 256, message = "Password must be between 8 and 256 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must include at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    private String password;

    @Override
    public String toString() {
        return "AuthRequestDto(username=" + username + ", password=******)";
    }
}
