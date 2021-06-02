package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;

import javax.validation.constraints.NotNull;

public interface CommentService {
    Comment getCommentById(Long id);
    Comment create(Comment comment);
    Comment update(Comment comment);
    boolean deleteCommentById(Long id);
}
