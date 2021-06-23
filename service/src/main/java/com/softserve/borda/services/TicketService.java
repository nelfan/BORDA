package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;

import java.util.List;
import java.util.Set;

public interface TicketService {
    Ticket getTicketById(Long id);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean deleteTicketById(Long id);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    List<Tag> getAllTagsByTicketId(Long ticketId);

    Ticket addTagToTicket(Long ticketId, Long tagId);

    Ticket deleteTagFromTicket(Long ticketId, Long tagId);

    boolean addMemberToTicket(Long ticketId, Long userId);

    boolean deleteMemberFromTicket(Long ticketId, Long userId);

    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);

    Ticket moveTicketToBoardColumn(Long boardColumnId, Long ticketId);

    Set<Ticket> getFilteredTicketsByTags(Long[] tagsId, Long board_column_id);
}
