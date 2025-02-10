package org.app.service;

import org.app.dto.auth.RegisterUserRequestDto;
import org.app.dto.auth.RegisterUserResponseDto;
import org.app.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    RegisterUserResponseDto saveUser(RegisterUserRequestDto userRequestDto);

    Optional<UserEntity> loadUserByUsernameOpt(String username);
}
