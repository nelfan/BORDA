package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;
import com.softserve.borda.repositories.CommentRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.CommentService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Tag> getAllCommentsByTicketId(Long ticketId) {
        return commentRepository.getAllCommentsByTicketId(ticketId);
    }

    @Override
    public Comment getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()) {
            return comment.get();
        }
        return null; // TODO: Throw custom exception
    }

    @Override
    public Comment createOrUpdate(Comment comment) {
        if (comment.getId() != null) {
            Optional<Comment> commentOptional = commentRepository.findById(comment.getId());

            if (commentOptional.isPresent()) {
                Comment newComment = commentOptional.get();
                newComment.setText(comment.getText());
                return commentRepository.save(newComment);
            }
        }
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public boolean addCommentToTicket(Comment comment, @NotNull Ticket ticket) {
        if (comment.getId() == null) {
            Ticket ticketEntity = ticketRepository.getOne(ticket.getId());
            comment.setTicket(ticketEntity);
            commentRepository.save(comment);
            ticketEntity.getComments().add(comment);
            ticketRepository.save(ticketEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean addCommentToUser(Comment comment, @NotNull User user) {
        if (comment.getId() == null) {
            User userEntity = userRepository.getOne(user.getId());
            comment.setUser(userEntity);
            commentRepository.save(comment);
            userEntity.getComments().add(comment);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}
