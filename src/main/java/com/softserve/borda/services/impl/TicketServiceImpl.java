package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Ticket;
import com.softserve.borda.services.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Override
    public List<Ticket> getAll() {
        return null;
    }

    @Override
    public Ticket getTicketById(Long id) {
        return null;
    }

    @Override
    public Ticket createOrUpdate(Ticket ticket) {
        return null;
    }

    @Override
    public void deleteTicketById(Long id) {

    }
}
