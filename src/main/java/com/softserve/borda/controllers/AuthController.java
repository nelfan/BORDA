package com.softserve.borda.controllers;

import com.softserve.borda.config.authorization.AuthRequest;
import com.softserve.borda.config.authorization.AuthResponse;
import com.softserve.borda.config.authorization.RegistrationRequest;
import com.softserve.borda.config.jwt.JwtProvider;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User registerUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        return userService.createOrUpdate(user);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(AuthRequest request) {
        User user = userService.getUserByUsername(request.getUsername());
        if (user != null &&
                (passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
            String token = jwtProvider.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        throw new AuthenticationCredentialsNotFoundException("Authentication failed");
    }
}
