package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CommentService {
    List<Tag> getAllCommentsByTicketId(Long ticketId);

    Comment getCommentById(Long id);
    Comment createOrUpdate(Comment comment);
    void deleteCommentById(Long id);

    boolean addCommentToTicket(Comment comment, @NotNull Ticket ticket);

    boolean addCommentToUser(Comment comment, @NotNull User user);
}
