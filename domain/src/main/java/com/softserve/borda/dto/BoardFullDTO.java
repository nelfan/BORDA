package com.softserve.borda.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardFullDTO {

    private Long id;
    private String name;
    private List<BoardColumnDTO> boardColumns;
    private List<UserBoardRelationDTO> userBoardRelations;
}
