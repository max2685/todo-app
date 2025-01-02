package org.app.config.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.dto.RegisterUserRequestDto;
import org.app.entities.UserEntity;
import org.app.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> findByUsername = userRepository.findByUsername(username);
        return findByUsername
                .map(user ->
                        User.builder()
                                .username(user.getUsername())
                                .password(passwordEncoder.encode(user.getPassword()))
                                .roles(getRoles(user.getRole()))
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserDetails saveUser(RegisterUserRequestDto userRequestDto) {
        UserEntity user = new UserEntity();
        user.setUsername(userRequestDto.getUsername());
        user.setRole(userRequestDto.getRole());
        user.setPassword(userRequestDto.getPassword());
        user = userRepository.save(user);

        return User.builder().username(user.getUsername()).password(user.getPassword()).roles(getRoles(user.getRole())).build();
    }

    public Optional<UserDetails> loadUserByUsernameOpt(String username) {
        Optional<UserEntity> findByUsername = userRepository.findByUsername(username);
        return findByUsername.map(user ->
                User.builder()
                        .username(user.getUsername())
                        .roles(getRoles(user.getRole()))
                        .build()
        );
    }

    private String[] getRoles(String roles) {
        if (roles == null) {
            return new String[]{"USER"};
        }
        return roles.split(",");
    }
}
