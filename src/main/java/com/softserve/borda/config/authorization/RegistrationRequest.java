package com.softserve.borda.config.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class RegistrationRequest {

    @NotEmpty
    private String username;

    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotEmpty
    private String password;
}
