package org.app.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.config.service.CustomUserDetailService;
import org.app.config.service.JWTService;
import org.app.dto.AuthRequestDto;
import org.app.dto.RegisterUserRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/public")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto user) {
        // todo: user can create themself with role ADMIN
        Optional<UserDetails> userOpt = userDetailService.loadUserByUsernameOpt(user.getUsername());
        if (userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already present");
        } else {
            UserDetails userDetails = userDetailService.saveUser(user);
            return ResponseEntity.ok(userDetails);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authenticationResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));

        log.info(authenticationResult.toString() + " -------- authenticationResult");

        if (authenticationResult.isAuthenticated()) {

            UserDetails userDetails = userDetailService.loadUserByUsername(authRequestDto.getUsername());
            log.info(authRequestDto.getUsername() + "---------- User found in database");

            String jwtToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok().body("Token: " + jwtToken);
        } else {
            throw new UsernameNotFoundException("Invalid username or password - " + authRequestDto.getUsername());
        }
    }
}

