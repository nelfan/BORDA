package com.softserve.borda.repositories;

import com.softserve.borda.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardColumnRepository boardColumnRepository;

    @Autowired
    BoardRepository boardRepository;

    Board board;
    BoardColumn boardColumn;
    Ticket ticket;
    User user;

    @BeforeEach
    void boardSetUp() {
        board = new Board();
        board.setName("Board1");
        boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        boardRepository.save(board);
        boardColumnRepository.save(boardColumn);
        ticket = new Ticket();
        ticket.setTitle("Ticket1");
        ticket.setDescription("Ticket1Body");
        ticketRepository.save(ticket);
        user = new User();
        user.setUsername("User1");
        user.setEmail("Email1");
        user.setPassword("pass1");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        userRepository.save(user);
    }

    @Test
    void shouldInsertAndReturnComment() {
        Comment comment = new Comment();
        comment.setText("Comment1");
        comment.setUser(user);
        Comment expected = commentRepository.save(comment);
        Comment actual = commentRepository.getOne(comment.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteComment() {
        Comment comment = new Comment();
        comment.setText("Comment1");
        comment.setUser(user);
        commentRepository.save(comment);
        commentRepository.deleteById(comment.getId());
        Assertions.assertFalse(commentRepository.findById(comment.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateComment() {
        Comment comment = new Comment();
        comment.setText("Comment1");
        comment.setUser(user);
        commentRepository.save(comment);
        comment.setText("Comment1Updated");
        Comment expected = commentRepository.save(comment);
        Comment actual = commentRepository.getOne(comment.getId());
        Assertions.assertEquals(expected, actual);
    }
}