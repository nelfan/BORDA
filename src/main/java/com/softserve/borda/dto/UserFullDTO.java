package com.softserve.borda.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserFullDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Byte[] avatar;
    private List<UserBoardRelationDTO> userBoardRelations;
    private Set<RoleDTO> roles;
    private List<CommentDTO> comments;
}
