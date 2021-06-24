package com.softserve.borda.services;

import com.softserve.borda.entities.Ticket;

import java.util.List;
import java.util.Set;

public interface TicketService {
    Ticket getTicketById(Long id);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean deleteTicketById(Long id);

    boolean addMemberToTicket(Long ticketId, Long userId);

    boolean deleteMemberFromTicket(Long ticketId, Long userId);

    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);

    Set<Ticket> getFilteredTicketsByTags(Long[] tagsId, Long board_column_id);

    Set<Ticket> getFilteredTicketsByMembers(Long[] membersId, Long board_id);

    Ticket moveTicketToBoardColumn(Long boardColumnId, Long ticketId, Double positionIndex);
}
