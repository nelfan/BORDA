package com.softserve.borda.exceptions;

import org.springframework.validation.BindingResult;

import java.util.Objects;

public class CustomValidationFailedException extends RuntimeException {
    public CustomValidationFailedException() {
        super("Validation failed");
    }

    public CustomValidationFailedException(String message) {
        super("Validation failed: \n" + message);
    }

    public CustomValidationFailedException(BindingResult bindingResult) {
        super("Validation failed in field: [" + Objects.requireNonNull(bindingResult.getFieldError()).getField() + "]" +
                " because value: [" + Objects.requireNonNull(bindingResult.getFieldError()).getRejectedValue() + "]" +
                " doesn't match the condition: [" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage() + "]"

        );
    }
}
