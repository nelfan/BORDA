package com.softserve.borda.services;

import com.softserve.borda.entities.*;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);
    Ticket createOrUpdate(Ticket ticket);
    void deleteTicketById(Long id);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    List<User> getAllMembersByTicketId(Long ticketId);

    List<Tag> getAllTagsByTicketId(Long id);

    Ticket addCommentToTicket(@NotNull Ticket ticket, Comment comment);

    Ticket addTagToTicket(@NotNull Ticket ticket, Tag tag);

    Ticket addMemberToTicket(@NotNull Ticket ticket, User member);
}
