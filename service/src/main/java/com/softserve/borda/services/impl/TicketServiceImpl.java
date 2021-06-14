package com.softserve.borda.services.impl;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CommentService commentService;
    private final TagService tagService;
    private final UserService userService;

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
            return false;
        }
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
    public List<Tag> getAllTagsByTicketId(Long ticketId) {
        return getTicketById(ticketId).getTags();
    }

    @Override
    public Ticket addCommentToTicket(Long ticketId, Long commentId) {
        Ticket ticket = getTicketById(ticketId);
        Comment comment = commentService.getCommentById(commentId);
        ticket.getComments().add(comment);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteCommentFromTicket(Long ticketId, Long commentId) {
        Ticket ticket = getTicketById(ticketId);
        Comment comment = commentService.getCommentById(commentId);
        ticket.getComments().remove(comment);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket addTagToTicket(Long ticketId, Long tagId) {
        Ticket ticket = getTicketById(ticketId);
        Tag tag = tagService.getTagById(tagId);
        ticket.getTags().add(tag);
        return ticketRepository.save(ticket);
    }

    public Ticket deleteTagFromTicket(Long ticketId, Long tagId) {
        Ticket ticket = getTicketById(ticketId);
        Tag tag = tagService.getTagById(tagId);
        ticket.getTags().remove(tag);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket addMemberToTicket(Long ticketId, Long userId) {
        Ticket ticket = getTicketById(ticketId);
        User user = userService.getUserById(userId);
        ticket.getMembers().add(user);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket deleteMemberFromTicket(Long ticketId, Long userId) {
        Ticket ticket = getTicketById(ticketId);
        User user = userService.getUserById(userId);
        ticket.getMembers().remove(user);
        return ticketRepository.save(ticket);
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
}
