package com.softserve.borda.services.impl;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
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
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new CustomEntityNotFoundException(Ticket.class));
    }

    @Override
    public Ticket createOrUpdate(Ticket ticket) {
        if (ticket.getId() != null) {
            Optional<Ticket> ticketOptional = ticketRepository.findById(ticket.getId());

            if (ticketOptional.isPresent()) {
                Ticket newTicket = ticketOptional.get();
                newTicket.setTitle(ticket.getTitle());
                newTicket.setDescription(ticket.getDescription());
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
            ticketRepository.save(ticket);
            boardListEntity.getTickets().add(ticket);
            boardListRepository.save(boardListEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getAllCommentsByTicketId(Long ticketId) {
        return getTicketById(ticketId).getComments();
    }

    @Override
    public List<User> getAllMembersByTicketId(Long ticketId) {
        return getTicketById(ticketId).getMembers();
    }

    @Override
    public List<Tag> getAllTagsByTicketId(Long id) {
        return getTicketById(id).getTags();
    }
}
