package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Invitation;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.InvitationRepository;
import com.softserve.borda.services.InvitationService;
import com.softserve.borda.services.UserBoardRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserBoardRelationService userBoardRelationService;

    @Override
    public Invitation getInvitationById(Long id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(Invitation.class));
    }

    @Override
    public Invitation create(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    @Override
    public boolean accept(Long invitationId) {
        try {
            Invitation invitation = getInvitationById(invitationId);
            UserBoardRelation userBoardRelation =
                    new UserBoardRelation();
            userBoardRelation.setBoard(invitation.getBoard());
            userBoardRelation.setUser(invitation.getReceiver());
            userBoardRelation.setUserBoardRole(invitation.getUserBoardRole());
            userBoardRelationService.create(userBoardRelation);
            invitation.setIsAccepted(true);
            invitationRepository.save(invitation);
            return true;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean decline(Long invitationId) {
        try {
            Invitation invitation = getInvitationById(invitationId);
            invitation.setIsAccepted(false);
            invitationRepository.save(invitation);
            return true;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Invitation> getAllByReceiverId(Long receiverId) {
        return invitationRepository.findAllByReceiverId(receiverId);
    }

    @Override
    public boolean deleteInvitationById(Long id) {
        try {
            invitationRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
    }

    @Override
    public Long getCountOfUnreadInvitation() {
        return invitationRepository.findByIsAccepted(null).stream().count();
    }
}
