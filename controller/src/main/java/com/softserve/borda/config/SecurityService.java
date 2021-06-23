package com.softserve.borda.config;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardColumnRepository;
import com.softserve.borda.repositories.CommentRepository;
import com.softserve.borda.repositories.TagRepository;
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

    private final TagRepository tagRepository;

    private final CommentRepository commentRepository;

    private final UserBoardRelationService userBoardRelationService;


    /**
     * Checks whether user has any relation(UserBoardRelation) with specific board.
     * Existence of relation between user and board allows user to view the contents of the board.
     * <p>
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The boardId argument is id of the Board entity.
     * <p>
     * This method will return true if user has any relation with a board,
     * no matter what role(UserBoardRole) user has with this board.
     * false will be returned in case user has no relation to
     * the board in question at all.
     *
     * @param authentication Authentication object from Spring Security
     * @param boardId        id of the Board entity
     * @return boolean value depending on whether logged in user has relation with
     * Board entity corresponding to the passed boardId
     */
    public boolean hasUserBoardRelation(Authentication authentication, Long boardId) {
        User user = userService.getUserByUsername(authentication.getName());

        return checkUserBoardRelation(user, boardId);
    }

    /**
     * Checks whether user has any relation(UserBoardRelation) with specific board.
     * Existence of relation between user and board allows user to view the contents of the board.
     * <p>
     * Returns a boolean value.
     * The userId argument is id of the User entity.
     * The boardId argument is id of the Board entity.
     * <p>
     * This method will return true if user has any relation with a board,
     * no matter what role(UserBoardRole) user has with this board.
     * false will be returned in case user has no relation to
     * the board in question at all.
     *
     * @param userId  id of the User entity
     * @param boardId id of the Board entity
     * @return boolean value depending on whether User entity corresponding to the passed userId
     * has relation with Board entity corresponding to the passed boardId
     */
    public boolean hasUserBoardRelation(Long userId, Long boardId) {
        User user = userService.getUserById(userId);
        return checkUserBoardRelation(user, boardId);
    }

    /**
     * Returns a boolean value.
     * The user argument is the User entity.
     * The boardId argument is id of the Board entity.
     * <p>
     * This method checks whether user has any relation with a board.
     * No matter what role(UserBoardRole) user has with this board,
     * if any exists, method will return true.
     * false will be returned in case user has no relation with
     * the board in question at all.
     *
     * @param user    User entity
     * @param boardId id of the Board entity
     * @return boolean value depending on whether User entity
     * has relation with Board entity corresponding to the passed boardId
     */
    private boolean checkUserBoardRelation(User user, Long boardId) {
        boolean result = false;
        try {
            userBoardRelationService.getUserBoardRelationByUserIdAndBoardId(user.getId(), boardId);
            result = true;
        } catch (CustomEntityNotFoundException e) {
            log.warning(e.getMessage());
        }
        return result;
    }

    /**
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The boardId argument is id of the Board entity.
     * The userBoardRoleIds argument is id of one or more roles(UserBoardRole) which user should have
     * <p>
     * This method will return true if user has relation with a board
     * with a role(UserBoardRole) corresponding to one of the role ids from parameter userBoardRoleIds.
     * false will be returned in case user has no necessary role with
     * the board in question.
     *
     * @param authentication   Authentication object from Spring Security
     * @param boardId          id of the Board entity
     * @param userBoardRoleIds ids of the UserBoardRoles entities
     * @return boolean value depending on whether logged in user has a necessary role(UserBoardRole)
     * with Board entity corresponding to the passed boardId;
     * role should match one of roles corresponding to the ids passed as userBoardRoleIds.
     */
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

    /**
     * Checks whether user has board management access.
     * Board management access includes editing and deleting board itself,
     * as well as editing and viewing of the contents of the board.
     * <p>
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The boardId argument is id of the Board entity.
     * <p>
     * This method will return true if user has relation with a board
     * with a role(UserBoardRole) = 'OWNER'.
     * Otherwise, false will be returned.
     *
     * @param authentication Authentication object from Spring Security
     * @param boardId        id of the Board entity
     * @return boolean value depending on whether logged in user has a
     * role(UserBoardRole) = 'OWNER'
     * with Board entity corresponding to the passed boardId.
     */
    public boolean hasBoardManagementAccess(Authentication authentication, Long boardId) {
        return hasUserBoardRole(authentication, boardId, UserBoardRole.UserBoardRoles.OWNER.getId());
    }

    /**
     * Checks whether user has board work access.
     * Board work access includes editing and viewing of the contents of the board.
     * <p>
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The boardId argument is id of the Board entity.
     * <p>
     * This method will return true if user has relation with a board
     * with a role(UserBoardRole) = 'COLLABORATOR'.
     * Otherwise, false will be returned.
     *
     * @param authentication Authentication object from Spring Security
     * @param boardId        id of the Board entity
     * @return boolean value depending on whether logged in user has a
     * role(UserBoardRole) = 'COLLABORATOR'
     * with Board entity corresponding to the passed boardId.
     */
    public boolean hasBoardWorkAccess(Authentication authentication, Long boardId) {
        return hasUserBoardRole(authentication, boardId, UserBoardRole.UserBoardRoles.COLLABORATOR.getId(), UserBoardRole.UserBoardRoles.OWNER.getId());
    }

    /**
     * Checks whether user has necessary role to invite another user to a specific board.
     * User with the role 'OWNER' may assign any role when sending an invite.
     * User with the role 'COLLABORATOR' may assign only 'COLLABORATOR' and 'VIEWER' roles when sending an invite.
     * User with the role 'VIEWER' may not send an invite.
     * <p>
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The boardId argument is id of the Board entity.
     * The userBoardRoleId argument is id of the UserBoardRole entity that user
     * tries to assign to invited user.
     * <p>
     * This method will return true if user has a necessary role
     * to assign the invite(see necessary roles above).
     * Otherwise, false will be returned.
     *
     * @param authentication  Authentication object from Spring Security
     * @param boardId         id of the Board entity
     * @param userBoardRoleId id of the UserBoardRole entity
     * @return boolean value depending on whether logged in user has a
     * necessary role
     * to assign the invite(see necessary roles above).
     */
    public boolean hasBoardInviteRights(Authentication authentication, Long boardId, Long userBoardRoleId) {
        if (userBoardRoleId.equals(UserBoardRole.UserBoardRoles.OWNER.getId())) {
            return hasBoardManagementAccess(authentication, boardId);
        }
        return hasBoardWorkAccess(authentication, boardId);
    }

    /**
     * Checks whether user is a receiver of an invite.
     * If the user is a receiver, they can accept an invite.
     * Otherwise, they are not allowed to do it.
     * <p>
     * Returns a boolean value.
     * The authentication argument is Authentication object from Spring Security.
     * Authentication is used to get username of currently logged in user
     * who is trying to send a request.
     * The invitationId argument is id of the Invitation entity.
     * <p>
     * This method will return true if user matches
     * the receiver of the Invitation entity.
     * Otherwise, false will be returned.
     *
     * @param authentication Authentication object from Spring Security
     * @param invitationId   id of the Invitation entity
     * @return boolean value depending on whether logged in user is a receiver of
     * the invite corresponding to the invitationId.
     */
    public boolean isUserAReceiver(Authentication authentication, Long invitationId) {
        Invitation invitation = invitationService.getInvitationById(invitationId);
        return invitation.getReceiver().getUsername().equals(authentication.getName());
    }

    /**
     * Checks whether Board entity owns the corresponding BoardColumn entity.
     * <p>
     * Returns a boolean value.
     * The boardId argument is id of the Board entity.
     * The columnId argument is id of the BoardColumn entity.
     * <p>
     * This method will return true if Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId.
     * Otherwise, false will be returned.
     *
     * @param boardId  id of the Board entity.
     * @param columnId id of the BoardColumn entity.
     * @return boolean value depending on whether Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId.
     */
    public boolean isColumnBelongsToBoard(Long boardId, Long columnId) {
        return boardColumnRepository.existsBoardColumnByIdAndBoardId(columnId, boardId);
    }

    /**
     * Checks whether Board entity owns the corresponding BoardColumn entity.
     * Checks whether BoardColumn entity owns the corresponding Ticket entity.
     * <p>
     * Returns a boolean value.
     * The boardId argument is id of the Board entity.
     * The columnId argument is id of the BoardColumn entity.
     * The ticketId argument is id of the Ticket entity.
     * <p>
     * This method will return true if Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * and
     * BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * Otherwise, false will be returned.
     *
     * @param boardId  id of the Board entity.
     * @param columnId id of the BoardColumn entity.
     * @param ticketId id of the Ticket entity.
     * @return boolean value depending on whether Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * and
     * BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     */
    public boolean isTicketBelongsToBoard(Long boardId, Long columnId, Long ticketId) {
        boolean ticketBelongsToColumn = isTicketBelongsToColumn(columnId, ticketId);
        return ticketBelongsToColumn && isColumnBelongsToBoard(boardId, columnId);
    }

    /**
     * Checks whether BoardColumn entity owns the corresponding Ticket entity.
     * <p>
     * Returns a boolean value.
     * The columnId argument is id of the BoardColumn entity.
     * The ticketId argument is id of the Ticket entity.
     * <p>
     * This method will return true if BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * Otherwise, false will be returned.
     *
     * @param columnId id of the BoardColumn entity.
     * @param ticketId id of the Ticket entity.
     * @return boolean value depending on whether BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     */
    private boolean isTicketBelongsToColumn(Long columnId, Long ticketId) {
        return ticketRepository.existsTicketByIdAndBoardColumnId(ticketId, columnId);
    }

    /**
     * Checks whether Board entity owns the corresponding BoardColumn entity.
     * Checks whether BoardColumn entity owns the corresponding Ticket entity.
     * Checks whether Ticket entity owns the corresponding Comment entity.
     * <p>
     * Returns a boolean value.
     * The boardId argument is id of the Board entity.
     * The columnId argument is id of the BoardColumn entity.
     * The ticketId argument is id of the Ticket entity.
     * The commentId argument is id of the Comment entity.
     * <p>
     * This method will return true if all are true:
     * 1) Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * 2) BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * 3) Ticket entity corresponding to the ticketId
     * is the owner of Comment entity corresponding to commentId.
     * Otherwise, false will be returned.
     *
     * @param boardId   id of the Board entity.
     * @param columnId  id of the BoardColumn entity.
     * @param ticketId  id of the Ticket entity.
     * @param commentId id of the Comment entity.
     * @return boolean value depending on whether all are true:
     * 1) Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * 2) BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * 3) Ticket entity corresponding to the ticketId
     * is the owner of Comment entity corresponding to commentId.
     */
    public boolean isCommentBelongsToBoard(Long boardId, Long columnId, Long ticketId, Long commentId) {
        boolean ticketBelongsToBoard = isTicketBelongsToBoard(boardId, columnId, ticketId);
        boolean commentBelongsToTicket = commentRepository.existsCommentByIdAndTicketId(commentId, ticketId);
        return ticketBelongsToBoard && commentBelongsToTicket;
    }

    /**
     * Checks whether Board entity owns the corresponding BoardColumn entity.
     * Checks whether BoardColumn entity owns the corresponding Ticket entity.
     * Checks whether Board entity owns the corresponding Tag entity.
     * Checks whether Ticket entity owns the corresponding Tag entity.
     * <p>
     * Returns a boolean value.
     * The boardId argument is id of the Board entity.
     * The columnId argument is id of the BoardColumn entity.
     * The ticketId argument is id of the Ticket entity.
     * The tagId argument is id of the Tag entity.
     * <p>
     * This method will return true if all are true:
     * 1) Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * 2) BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * 3) Board entity corresponding to the boardId
     * is the owner of Tag entity corresponding to tagId.
     * 4) Ticket entity corresponding to the ticketId
     * is the owner of Tag entity corresponding to tagId.
     * Otherwise, false will be returned.
     *
     * @param boardId  id of the Board entity.
     * @param columnId id of the BoardColumn entity.
     * @param ticketId id of the Ticket entity.
     * @param tagId    id of the Tag entity.
     * @return boolean value depending on whether all are true:
     * 1) Board entity corresponding to the boardId
     * is the owner of BoardColumn entity corresponding to columnId
     * 2) BoardColumn entity corresponding to the columnId
     * is the owner of Ticket entity corresponding to ticketId.
     * 3) Board entity corresponding to the boardId
     * is the owner of Tag entity corresponding to tagId.
     * 4) Ticket entity corresponding to the ticketId
     * is the owner of Tag entity corresponding to tagId.
     */
    public boolean isTagBelongsToTicket(Long boardId, Long columnId, Long ticketId, Long tagId) {
        boolean tagBelongsToBoard = tagRepository.existsTagByIdAndBoardId(tagId, boardId);
        boolean tagBelongsToTicket = tagRepository.existsTagByIdAndTicketId(tagId, ticketId);
        boolean ticketBelongsToBoard = isTicketBelongsToBoard(boardId, columnId, ticketId);
        return ticketBelongsToBoard && tagBelongsToBoard && tagBelongsToTicket;
    }

    /**
     * Checks whether Board entity owns the corresponding Tag entity.
     * <p>
     * Returns a boolean value.
     * The boardId argument is id of the Board entity.
     * The tagId argument is id of the Tag entity.
     * <p>
     * This method will return true if Board entity corresponding to the boardId
     * is the owner of Tag entity corresponding to tagId.
     * Otherwise, false will be returned.
     *
     * @param boardId id of the Board entity.
     * @param tagId   id of the Tag entity.
     * @return boolean value depending on whether is the Board entity corresponding to the boardId
     * is the owner of Tag entity corresponding to tagId.
     */
    public boolean isTagBelongsToBoard(Long boardId, Long tagId) {
        return tagRepository.existsTagByIdAndBoardId(tagId, boardId);
    }
}
