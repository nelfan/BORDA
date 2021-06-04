package com.softserve.borda.dto;

import lombok.Data;

@Data
public class InvitationDTO {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private Long boardId;

    private Long userBoardRoleId;

    private Boolean isAccepted;
}
