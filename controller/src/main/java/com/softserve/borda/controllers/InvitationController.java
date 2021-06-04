package com.softserve.borda.controllers;

import com.softserve.borda.dto.InvitationAcceptDTO;
import com.softserve.borda.dto.InvitationDTO;
import com.softserve.borda.entities.Invitation;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.InvitationService;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/invitations")
@AllArgsConstructor
@Log
@CrossOrigin
public class InvitationController {

    private final ModelMapper modelMapper;

    private final InvitationService invitationService;

    private final UserService userService;

    private final BoardService boardService;

    private final UserBoardRelationService userBoardRelationService;

    @GetMapping
    public List<InvitationDTO> getAllInvitation(
            Authentication authentication) {
        var user = userService.getUserByUsername(authentication.getName());
        List<Invitation> invitations = invitationService.getAllByReceiverId(user.getId());

        List<InvitationDTO> invitationDTOs = invitations.stream().map(
                invitation -> modelMapper.map(invitation,
                InvitationDTO.class)).collect(Collectors.toList());

        return invitationDTOs;
    }

    @PostMapping("/{userId}/boards/{boardId}/roles/{userBoardRoleId}")
    public InvitationDTO createInvitation(Authentication authentication,
                                          @PathVariable Long userId,
                                          @PathVariable Long boardId,
                                          @PathVariable Long userBoardRoleId) {
        var user = userService.getUserByUsername(authentication.getName());
        Invitation invitation = new Invitation();
        invitation.setSender(user);
        invitation.setReceiver(userService.getUserById(userId));
        invitation.setBoard(boardService.getBoardById(boardId));
        invitation.setUserBoardRole(
                userBoardRelationService.getUserBoardRoleById(userBoardRoleId));
        invitation = invitationService.create(invitation);
        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);
        return invitationDTO;
    }


    @PostMapping("/{invitationId}")
    public ResponseEntity<Boolean> acceptInvitation(@PathVariable Long invitationId,
                             @RequestBody InvitationAcceptDTO acceptDTO) {
        Boolean isAccepted = acceptDTO.getIsAccepted();
        if(Objects.isNull(isAccepted)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(isAccepted) {
            return ResponseEntity.ok(invitationService.accept(invitationId));
        } else {
            return ResponseEntity.ok(invitationService.decline(invitationId));
        }
    }
}
