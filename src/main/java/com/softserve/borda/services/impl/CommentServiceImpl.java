package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.services.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public List<Comment> getAll() {
        return null;
    }

    @Override
    public Comment getCommentById(Long id) {
        return null;
    }

    @Override
    public Comment createOrUpdate(Comment comment) {
        return null;
    }

    @Override
    public void deleteCommentById(Long id) {

    }
}
