package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;

public interface CommentService {
    Comment getCommentById(Long id);

    Comment create(Comment comment);

    Comment update(Comment comment);

    boolean deleteCommentById(Long id);
}
