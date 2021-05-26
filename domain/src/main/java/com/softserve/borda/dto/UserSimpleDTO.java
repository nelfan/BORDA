package com.softserve.borda.dto;

import lombok.Data;

@Data
public class UserSimpleDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private byte[] avatar;

}