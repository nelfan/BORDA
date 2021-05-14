package com.softserve.borda.dto;

import lombok.Data;

@Data
public class CreateUserDTO {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Byte[] avatar;
}
