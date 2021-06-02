package com.softserve.borda.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationFailedException extends AuthenticationException {
    public CustomAuthenticationFailedException() {
        super("Authentication failed");
    }

    public CustomAuthenticationFailedException(String message) {
        super(message);
    }

    public CustomAuthenticationFailedException(String message, Throwable t) {
        super(message, t);
    }
}