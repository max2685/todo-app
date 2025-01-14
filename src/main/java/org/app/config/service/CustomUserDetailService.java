package org.app.config.service;

import lombok.AllArgsConstructor;
import org.app.entities.UserEntity;
import org.app.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> findByUsername = userRepository.findByUsername(username);
        return findByUsername
                .map(user ->
                        User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole().getAuthority())
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
