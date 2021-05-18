package com.softserve.borda.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserBoardRelationDTO {

    private Long id;
    private Long userId;
    private Long boardId;
    private BoardRoleDTO boardRole;

}
