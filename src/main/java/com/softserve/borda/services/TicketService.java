package com.softserve.borda.services;

import com.softserve.borda.entities.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getAll();
    Ticket getTicketById(Long id);
    Ticket createOrUpdate(Ticket ticket);
    void deleteTicketById(Long id);
}
