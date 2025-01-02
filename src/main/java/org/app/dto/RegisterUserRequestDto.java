package org.app.dto;

import lombok.Data;

@Data
public class RegisterUserRequestDto {
    private String username;
    private String password;
    private String role;
}
