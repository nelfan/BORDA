package com.softserve.borda.services;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.repositories.CommentRepository;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository, 
                ticketRepository, 
                userRepository);
    }

    @Test
    void shouldGetAllCommentsByTicketId() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Comment comment = new Comment();
            comment.setId((long) i);
            comment.setText("comment" + i);
            comments.add(comment);
        }

        when(commentRepository.getAllCommentsByTicketId(1L)).thenReturn(comments);

        List<Comment> commentList = commentService.getAllCommentsByTicketId(1L);

        assertEquals(3, commentList.size());
        verify(commentRepository, times(1)).getAllCommentsByTicketId(1L);
    }

    @Test
    void shouldGetCommentById() {
        Comment expected = new Comment();
        expected.setId(1L);
        expected.setText("comment");

        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        Comment actual = commentService.getCommentById(1L);
        assertEquals(actual, expected);
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateComment() {
        Comment comment = new Comment();
        comment.setText("comment");

        Comment expected = new Comment();
        expected.setId(1L);
        expected.setText("comment");

        when(commentRepository.save(comment)).thenReturn(expected);

        Comment actual = commentService.createOrUpdate(comment);

        assertEquals(expected, actual);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void shouldUpdateComment() {
        Comment comment = new Comment();
        comment.setText("comment");

        Comment commentSaved = new Comment();
        commentSaved.setId(1L);
        commentSaved.setText("comment");

        Comment commentUpdated = new Comment();
        commentUpdated.setId(1L);
        commentUpdated.setText("commentUpdated");

        when(commentRepository.save(comment)).thenReturn(commentSaved);

        when(commentRepository.save(commentUpdated)).thenReturn(commentUpdated);

        commentService.createOrUpdate(comment);

        Comment actual = commentService.createOrUpdate(commentUpdated);

        assertEquals(commentUpdated, actual);
        verify(commentRepository, times(1)).save(comment);
        verify(commentRepository, times(1)).save(commentUpdated);
    }

    @Test
    void shouldDeleteCommentById() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        commentService.deleteCommentById(comment.getId());

        assertNull(commentService.getCommentById(1L));
        verify(commentRepository, times(1)).deleteById(comment.getId());
    }
}