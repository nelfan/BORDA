package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();
    Comment getCommentById(Long id);
    Comment createOrUpdate(Comment comment);
    void deleteCommentById(Long id);
}
