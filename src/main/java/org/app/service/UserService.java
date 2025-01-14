package org.app.service;

import org.app.dto.RegisterUserRequestDto;
import org.app.dto.RegisterUserResponseDto;
import org.app.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    RegisterUserResponseDto saveUser(RegisterUserRequestDto userRequestDto);

    Optional<UserEntity> loadUserByUsernameOpt(String username);
}
