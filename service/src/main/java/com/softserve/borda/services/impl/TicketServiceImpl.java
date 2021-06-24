package com.softserve.borda.services.impl;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.*;
import com.softserve.borda.specifications.TicketNoTaggedSpecification;
import com.softserve.borda.specifications.TicketWithNoMembersSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new CustomEntityNotFoundException(Ticket.class));
    }

    @Override
    public Ticket create(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket update(Ticket ticket) {
        Ticket existingTicket = getTicketById(ticket.getId());
        existingTicket.setTitle(ticket.getTitle());
        existingTicket.setDescription(ticket.getDescription());
        return ticketRepository.save(existingTicket);
    }

    @Override
    public boolean deleteTicketById(Long id) {
        try {
            ticketRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(Ticket.class);
        }
    }

    public List<Tag> getAllTagsByTicketId(Long ticketId) {
        return getTicketById(ticketId).getTags();
    }

    @Transactional
    @Override
    public boolean addMemberToTicket(Long ticketId, Long userId) {
        return ticketRepository.createTicketMemberRow(ticketId, userId) == 1;
    }

    @Transactional
    @Override
    public boolean deleteMemberFromTicket(Long ticketId, Long userId) {
        return ticketRepository.deleteTicketMemberRow(ticketId, userId) == 1;
    }

    @Override
    public List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId) {
        return ticketRepository.getAllTicketsByBoardColumnId(boardColumnId);
    }

    @Override
    public Ticket moveTicketToBoardColumn(Long boardColumnId, Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        ticket.setBoardColumnId(boardColumnId);
        return update(ticket);
    }

    @Override
    public Set<Ticket> getFilteredTicketsByTags(Long[] tagsId, Long board_id) {
        Set<Ticket> tickets = new HashSet<>();
        if(Arrays.stream(tagsId).anyMatch(t -> t==0)) {
            tickets.addAll(
                    ticketRepository.findAllByBoardId(board_id).stream().filter(x ->
                            ticketRepository.findAll(new TicketNoTaggedSpecification()).stream().anyMatch(y -> y == x)).collect(Collectors.toList()));
        }
        if(Arrays.stream(tagsId).filter(t -> t!=0).count()>0)
            tickets.addAll(ticketRepository.getAllTicketsByTags(Arrays.stream(tagsId).filter(t->t!=0).toArray(Long[]::new), board_id));
        return tickets;
    }

    @Override
    public Set<Ticket> getFilteredTicketsByMembers(Long[] membersId, Long board_id) {
        Set<Ticket> tickets = new HashSet<>();
        if(Arrays.stream(membersId).anyMatch(t -> t==0)) {
            tickets.addAll(
                    ticketRepository.findAllByBoardId(board_id).stream().filter(x ->
                            ticketRepository.findAll(new TicketWithNoMembersSpecification()).stream().anyMatch(y -> y == x)).collect(Collectors.toList()));
        }
        if(Arrays.stream(membersId).filter(t -> t!=0).count()>0)
            tickets.addAll(ticketRepository.getAllTicketsByMembers(Arrays.stream(membersId).filter(t->t!=0).toArray(Long[]::new), board_id));
        return tickets;
    }
}
