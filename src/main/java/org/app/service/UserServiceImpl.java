package org.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.config.Role;
import org.app.dto.RegisterUserRequestDto;
import org.app.dto.RegisterUserResponseDto;
import org.app.entities.UserEntity;
import org.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserResponseDto saveUser(RegisterUserRequestDto userRequestDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userRequestDto.getUsername());
        user.setRole(Role.CLIENT);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user = userRepository.save(user);

        return RegisterUserResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole()).build();
    }

    public Optional<UserEntity> loadUserByUsernameOpt(String username) {
        return userRepository.findByUsername(username);
    }
}
