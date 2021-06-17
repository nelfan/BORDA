package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;

import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean deleteTicketById(Long id);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    List<User> getAllMembersByTicketId(Long ticketId);

    Ticket addCommentToTicket(Long ticketId, Long commentId);

    Ticket deleteCommentFromTicket(Long ticketId, Long commentId);

    Ticket addMemberToTicket(Long ticketId, Long userId);

    Ticket deleteMemberFromTicket(Long ticketId, Long userId);

    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);

    Ticket moveTicketToBoardColumn(Long boardColumnId, Long ticketId);
}
