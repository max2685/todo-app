package org.app.dto;

import lombok.Builder;
import lombok.Data;
import org.app.config.Role;

@Data
@Builder
public class RegisterUserResponseDto {
    private String username;
    private String password;
    private Role role;
}
