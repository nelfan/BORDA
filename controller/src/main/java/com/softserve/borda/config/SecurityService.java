package com.softserve.borda.config;

import com.softserve.borda.entities.Invitation;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardColumnRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.InvitationService;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log
public class SecurityService {

    private final UserService userService;

    private final InvitationService invitationService;

    private final BoardColumnRepository boardColumnRepository;

    private final TicketRepository ticketRepository;

    private final UserBoardRelationService userBoardRelationService;

    public boolean hasUserBoardRelation(Authentication authentication, Long boardId) {
        User user = userService.getUserByUsername(authentication.getName());
        boolean result = false;
        try {
            userBoardRelationService.getUserBoardRelationByUserIdAndBoardId(user.getId(), boardId);
            result = true;
        } catch (CustomEntityNotFoundException e) {
            log.warning(e.getMessage());
        }
        return result;
    }

    private boolean hasUserBoardRole(Authentication authentication, Long boardId,
                                     Long... userBoardRoleIds) {
        User user = userService.getUserByUsername(authentication.getName());
        boolean result = false;
        try {
            UserBoardRelation userBoardRelation = userBoardRelationService.getUserBoardRelationByUserIdAndBoardId(user.getId(), boardId);
            for (Long userBoardRoleId :
                    userBoardRoleIds) {
                result = result || userBoardRelation.getUserBoardRoleId().equals(userBoardRoleId);
            }
        } catch (CustomEntityNotFoundException e) {
            log.warning(e.getMessage());
        }
        return result;
    }

    public boolean hasBoardManagementAccess(Authentication authentication, Long boardId) {
        return hasUserBoardRole(authentication, boardId, 1L);
    }

    public boolean hasBoardWorkAccess(Authentication authentication, Long boardId) {
        return hasUserBoardRole(authentication, boardId, 2L, 1L);
    }

    public boolean hasBoardInviteRights(Authentication authentication, Long boardId, Long userBoardRoleId) {
        if(userBoardRoleId.equals(1L)) {
            return hasBoardManagementAccess(authentication, boardId);
        }
        return hasBoardWorkAccess(authentication, boardId);
    }

    public boolean isColumnBelongsToBoard(Long boardId, Long columnId) {
        return boardColumnRepository.existsBoardColumnByIdAndBoardId(columnId, boardId);
    }

    public boolean isTicketBelongsToBoard(Long boardId, Long columnId, Long ticketId) {
        boolean ticketBelongsToColumn = ticketRepository.existsTicketByIdAndBoardColumnId(ticketId, columnId);
        return ticketBelongsToColumn && isColumnBelongsToBoard(boardId, columnId);
    }

    public boolean isUserAReceiver(Authentication authentication, Long invitationId) {
        Invitation invitation = invitationService.getInvitationById(invitationId);
        return invitation.getReceiver().getUsername().equals(authentication.getName());
    }
}
