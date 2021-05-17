package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.CommentRepository;
import com.softserve.borda.repositories.TagRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

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

    @Override
    public Ticket addCommentToTicket(Long ticketId, Comment comment) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getComments().add(comment);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteCommentFromTicket(Long ticketId, Comment comment) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getComments().remove(comment);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket addTagToTicket(Long ticketId, Tag tag) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getTags().add(tag);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteTagFromTicket(Long ticketId, Tag tag) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getTags().remove(tag);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket addMemberToTicket(Long ticketId, User user) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getMembers().add(user);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteMemberFromTicket(Long ticketId, User user) {
        Ticket ticket = getTicketById(ticketId);
        ticket.getMembers().remove(user);
        return ticketRepository.save(ticket);
    }
}
