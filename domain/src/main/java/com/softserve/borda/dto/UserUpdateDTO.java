package com.softserve.borda.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private byte[] avatar;
}
