package com.softserve.borda.controllers;

import com.softserve.borda.authorization.AuthRequest;
import com.softserve.borda.authorization.AuthResponse;
import com.softserve.borda.authorization.RegistrationRequest;
import com.softserve.borda.config.jwt.JwtProvider;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setEmail(registrationRequest.getEmail());
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            userService.create(user);
            AuthRequest authRequest = modelMapper.map(registrationRequest, AuthRequest.class);
            return auth(authRequest);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        try {
            User user = userService.getUserByUsername(request.getUsername());
            if (user != null &&
                    (passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
                String token = jwtProvider.generateToken(user.getUsername());
                AuthResponse authResponse = new AuthResponse(token);
                return ResponseEntity.ok(authResponse);
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
