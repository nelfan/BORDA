package com.softserve.borda.controllers;

import com.softserve.borda.config.authorization.AuthRequest;
import com.softserve.borda.config.authorization.AuthResponse;
import com.softserve.borda.config.authorization.RegistrationRequest;
import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.config.jwt.JwtProvider;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Log
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtConvertor jwtConvertor;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(RegistrationRequest registrationRequest) {
        try {
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setEmail(registrationRequest.getEmail());
            user.setFirstName(registrationRequest.getUsername());
            user.setLastName(registrationRequest.getLastName());
            user.setAvatar(registrationRequest.getAvatar().getBytes());
            userService.createOrUpdate(user);
            jwtConvertor.saveUser(user);
            return auth(modelMapper.map(registrationRequest, AuthRequest.class));
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(AuthRequest request) {
        try {
            User user = userService.getUserByUsername(request.getUsername());
            if (user != null &&
                    (passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
                String token = jwtProvider.generateToken(user.getUsername());
                return ResponseEntity.ok(new AuthResponse(token));
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
