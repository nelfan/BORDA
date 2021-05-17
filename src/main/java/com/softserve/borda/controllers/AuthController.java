package com.softserve.borda.controllers;

import com.softserve.borda.config.authorization.AuthRequest;
import com.softserve.borda.config.authorization.AuthResponse;
import com.softserve.borda.config.authorization.RegistrationRequest;
import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.config.jwt.JwtProvider;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JwtConvertor jwtConvertor;

    @Autowired
    ModelMapper modelMapper;

    public AuthController(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, JwtConvertor jwtConvertor) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.jwtConvertor = jwtConvertor;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        userService.createOrUpdate(user);
        jwtConvertor.saveUser(user);
        return auth(modelMapper.map(registrationRequest, AuthRequest.class));
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
