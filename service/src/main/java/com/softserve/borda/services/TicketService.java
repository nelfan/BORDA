package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Ticket;

import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean deleteTicketById(Long id);

    boolean addMemberToTicket(Long ticketId, Long userId);

    boolean deleteMemberFromTicket(Long ticketId, Long userId);

    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);

    Ticket moveTicketToBoardColumn(Long boardColumnId, Long ticketId);
}
