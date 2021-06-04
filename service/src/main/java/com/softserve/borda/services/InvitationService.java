package com.softserve.borda.services;

import com.softserve.borda.entities.Invitation;
import com.softserve.borda.entities.User;

import java.util.List;

public interface InvitationService {

    Invitation getInvitationById(Long id);
    Invitation create(Invitation invitation);
    boolean acceptOrDecline(Invitation invitation);
    List<Invitation> getAllByUser(User user);
    boolean deleteInvitationById(Long id);
    Long getCountOfUnreadInvitation();
}
