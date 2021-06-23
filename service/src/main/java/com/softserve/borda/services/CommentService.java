package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment getCommentById(Long id);

    Comment create(Comment comment);

    Comment update(Comment comment);

    boolean deleteCommentById(Long id);

    List<Comment> getAllCommentsByTicketId(Long ticketId);
}
