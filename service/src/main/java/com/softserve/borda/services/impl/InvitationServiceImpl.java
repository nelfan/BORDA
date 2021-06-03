package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Invitation;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.InvitationRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.InvitationService;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

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
    public boolean acceptOrDecline(Invitation invitation) {
        Invitation existingInvitation = invitationRepository.findById(invitation.getId())
                .orElseThrow(() -> new CustomEntityNotFoundException(Invitation.class));
        if(invitation.getIsAccepted().booleanValue()){
            UserBoardRelation userBoardRelation =
                    new UserBoardRelation();
            userBoardRelation.setBoard(invitation.getBoard());
            userBoardRelation.setUser(invitation.getUser());
            userBoardRelation.setBoardRole(invitation.getBoardRole());
            userBoardRelationService.createOrUpdate(userBoardRelation);
        }
        existingInvitation.setIsAccepted(invitation.getIsAccepted());
        try {
            invitationRepository.save(existingInvitation);
            return true;
        }
        catch (Exception e){
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Invitation> getAllByUser(User user) {
        return invitationRepository.findAllByUser(user);
    }

    @Override
    public boolean deleteInvitationById(Long id) {
        try {
            invitationRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public Long getCountOfUnreadInvitation() {
        return invitationRepository.findByIsAccepted(null).stream().count();
    }
}
