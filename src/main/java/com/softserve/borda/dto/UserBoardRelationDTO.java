package com.softserve.borda.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserBoardRelationDTO {

    private Long id;
    private UserFullDTO user;
    private BoardFullDTO board;
    private List<BoardRoleDTO> boardRoles;
}
