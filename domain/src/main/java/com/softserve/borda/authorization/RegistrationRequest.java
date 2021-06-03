package com.softserve.borda.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

import com.softserve.borda.validation.ValidationUtils;

@Data
@AllArgsConstructor
public class RegistrationRequest {

    @NotEmpty
    @Size(min = 3, message = "Username cannot be less than 3 characters")
    @Size(max = 30, message = "Username cannot be more than 30 characters")
    @Pattern(regexp = ValidationUtils.REG_EXP_USERNAME,
            message = "Username can only contain letters, numbers and \"_\"")
    private String username;

    @NotBlank
    @Size(min = 2, message = "First name cannot be less than 2 characters")
    @Size(max = 30, message = "First name cannot be more than 30 characters")
    @Pattern(regexp = ValidationUtils.REG_EXP_FIRST_NAME,
            message = "First name can only contain letters and should start with Upper case")
    private String firstName;

    @NotBlank
    @Size(min = 2, message = "Last name cannot be less than 2 characters")
    @Size(max = 30, message = "Last name cannot be more than 30 characters")
    @Pattern(regexp = ValidationUtils.REG_EXP_LAST_NAME,
            message = "Last name can only contain letters and should start with Upper case")
    private String lastName;

    @NotBlank
    @Email(message = "invalid email format")
    private String email;

    @NotEmpty
    @Size(min = 6, message = "Password cannot be less than 2 characters")
    @Size(max = 30, message = "Password cannot be more than 30 characters")
    @Pattern(regexp = ValidationUtils.REG_EXP_PASSWORD,
            message = "Password should have at least one uppercase letter, one lowercase letter and one number")
    private String password;
}