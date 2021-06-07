package com.softserve.borda.dto;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
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
