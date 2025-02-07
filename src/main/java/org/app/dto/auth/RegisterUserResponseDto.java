package org.app.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.app.config.Role;

@Data
@Builder
public class RegisterUserResponseDto {
    private String username;
    private Role role;
}
