package com.softserve.borda.services.impl;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.repositories.BoardListRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.TicketService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final BoardListRepository boardListRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, BoardListRepository boardListRepository) {
        this.ticketRepository = ticketRepository;
        this.boardListRepository = boardListRepository;
    }


    @Override
    public List<Ticket> getAllTicketsByBoardListId(Long boardListId) {
        return ticketRepository.getAllTicketsByBoardListId(boardListId);
    }

    @Override
    public Ticket getTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isPresent()) {
            return ticket.get();
        }
        return null; // TODO: Throw custom exception
    }

    @Override
    public Ticket createOrUpdate(Ticket ticket) {
        if (ticket.getId() != null) {
            Optional<Ticket> ticketOptional = ticketRepository.findById(ticket.getId());

            if (ticketOptional.isPresent()) {
                Ticket newTicket = ticketOptional.get();
                newTicket.setName(ticket.getName());
                newTicket.setBody(ticket.getBody());
                return ticketRepository.save(newTicket);
            }
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public boolean addTicketToBoardList(Ticket ticket, @NotNull BoardList boardList) {
        if (ticket.getId() == null) {
            BoardList boardListEntity = boardListRepository.getOne(boardList.getId());
            ticket.setBoardList(boardListEntity);
            ticketRepository.save(ticket);
            boardListEntity.getTickets().add(ticket);
            boardListRepository.save(boardListEntity);
            return true;
        }
        return false;
    }
}
