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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JwtConvertor jwtConvertor;

    public AuthController(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/auth")
    public String getAuthPage() {
        return "login_page";
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
        return auth(modelMapper.map(user, AuthRequest.class));
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
