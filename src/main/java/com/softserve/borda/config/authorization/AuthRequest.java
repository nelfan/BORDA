package com.softserve.borda.config.authorization;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
