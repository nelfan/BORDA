package com.softserve.borda.services;

import com.softserve.borda.entities.*;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);
    Ticket createOrUpdate(Ticket ticket);
    void deleteTicketById(Long id);
    boolean addTicketToBoardList(Ticket ticket, @NotNull BoardList boardList);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    List<User> getAllMembersByTicketId(Long ticketId);

    List<Tag> getAllTagsByTicketId(Long id);
}
