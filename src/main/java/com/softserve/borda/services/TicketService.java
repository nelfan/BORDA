package com.softserve.borda.services;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TicketService {
    List<Ticket> getAllTicketsByBoardListId(Long id);
    Ticket getTicketById(Long id);
    Ticket createOrUpdate(Ticket ticket);
    void deleteTicketById(Long id);
    boolean addTicketToBoardList(Ticket ticket, @NotNull BoardList boardList);
}
