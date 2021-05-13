package com.softserve.borda.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Byte[] avatar;
}
