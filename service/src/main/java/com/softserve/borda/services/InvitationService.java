package com.softserve.borda.services;

import com.softserve.borda.entities.Invitation;

import java.util.List;

public interface InvitationService {

    Invitation getInvitationById(Long id);
    Invitation create(Invitation invitation);
    boolean accept(Long invitationId);
    boolean decline(Long invitationId);

    List<Invitation> getAllByReceiverId(Long receiverId);

    boolean deleteInvitationById(Long id);
    Long getCountOfUnreadInvitation();
}
