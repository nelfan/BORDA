package com.softserve.borda.dto;

import lombok.Data;

@Data
public class UserGetDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
//    private List<UserBoardRelation> userBoardRelations;
//    private Set<Role> roles;
//    private List<Comment> comments;

}