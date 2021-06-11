package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.CommentRepository;
import com.softserve.borda.services.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(Comment.class));
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        Comment existingComment = getCommentById(comment.getId());
        existingComment.setText(comment.getText());
        return commentRepository.save(existingComment);
    }

    @Override
    public boolean deleteCommentById(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(Comment.class);
        }
    }
}
