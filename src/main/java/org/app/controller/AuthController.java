package org.app.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.config.service.CustomUserDetailService;
import org.app.config.service.JWTService;
import org.app.dto.AuthRequestDto;
import org.app.dto.RegisterUserRequestDto;
import org.app.dto.RegisterUserResponseDto;
import org.app.entities.UserEntity;
import org.app.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/public")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final UserServiceImpl userService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequestDto user) {
        Optional<UserEntity> userOpt = userService.loadUserByUsernameOpt(user.getUsername());
        if (userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already present");
        } else {
            RegisterUserResponseDto userDetails = userService.saveUser(user);
            return ResponseEntity.ok(userDetails);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authenticationResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));

        if (authenticationResult.isAuthenticated()) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(authRequestDto.getUsername());
            String jwtToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok().body("Bearer " + jwtToken);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}

