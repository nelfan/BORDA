package com.softserve.borda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class InvitationDTO {

    private Long id;

    private UserSimpleDTO sender;

    private Long receiverId;

    private BoardSimpleDTO board;

    private UserBoardRoleDTO userBoardRole;

    private Boolean isAccepted;
}
