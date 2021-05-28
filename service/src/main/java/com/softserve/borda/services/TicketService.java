package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;

import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);
    Ticket createOrUpdate(Ticket ticket);
    void deleteTicketById(Long id);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    List<User> getAllMembersByTicketId(Long ticketId);

    List<Tag> getAllTagsByTicketId(Long id);
  
    Ticket addCommentToTicket(Long ticketId, Comment comment);

    Ticket deleteCommentFromTicket(Long ticketId, Comment comment);

    Ticket addTagToTicket(Long ticketId, Tag tag);

    Ticket deleteTagFromTicket(Long ticketId, Tag tag);

    Ticket addMemberToTicket(Long ticketId, User user);

    Ticket deleteMemberFromTicket(Long ticketId, User user);
  
}
