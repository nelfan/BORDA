package com.softserve.borda.dto;

import lombok.Data;

@Data
public class UserPostDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Byte[] avatar;
}
