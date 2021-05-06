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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardListRepository boardListRepository;

    @Autowired
    BoardRepository boardRepository;

    Board board;
    BoardList boardList;

    @BeforeEach
    void boardSetUp() {
        board = new Board();
        board.setName("Board1");
        boardList = new BoardList();
        boardList.setName("BoardList1");
        boardList.setBoard(board);
        boardRepository.save(board);
        boardListRepository.save(boardList);
    }

    @Test
    void shouldInsertAndReturnTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("Ticket1");
        ticket.setBody("Ticket1Body");
        ticket.setBoardList(boardList);
        Ticket expected = ticketRepository.save(ticket);
        Ticket actual = ticketRepository.getOne(ticket.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("Ticket1");
        ticket.setBody("Ticket1Body");
        ticket.setBoardList(boardList);
        ticketRepository.save(ticket);
        ticketRepository.deleteById(ticket.getId());
        Assertions.assertFalse(ticketRepository.findById(ticket.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("Ticket1");
        ticket.setBody("Ticket1Body");
        ticket.setBoardList(boardList);
        ticketRepository.save(ticket);
        ticket.setName("Ticket1updated");
        Ticket expected = ticketRepository.save(ticket);
        Ticket actual = ticketRepository.getOne(ticket.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSaveCommentsWithTicket() {
        User user = new User();
        user.setUsername("User1");
        user.setEmail("Email1");
        user.setPassword("pass1");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        userRepository.save(user);
        Ticket ticket = new Ticket();
        ticket.setName("Ticket1");
        ticket.setBody("Ticket1Body");
        ticket.setBoardList(boardList);
        Comment comment1 = new Comment();
        comment1.setText("Comment1Text");
        comment1.setTicket(ticket);
        comment1.setUser(user);
        Comment comment2 = new Comment();
        comment2.setText("Comment2Text");
        comment2.setTicket(ticket);
        comment2.setUser(user);
        ticket.getComments().add(comment1);
        ticket.getComments().add(comment2);
        ticketRepository.save(ticket);
        Ticket actualTicket = ticketRepository.getOne(ticket.getId());
        List<Comment> expected = new ArrayList<>();
        expected.add(comment1);
        expected.add(comment2);
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertEquals(ticket, actualTicket);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteCommentsWithTicket() {
        User user = new User();
        user.setUsername("User1");
        user.setEmail("Email1");
        user.setPassword("pass1");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        userRepository.save(user);
        Ticket ticket = new Ticket();
        ticket.setName("Ticket1");
        ticket.setBody("Ticket1Body");
        ticket.setBoardList(boardList);
        Comment comment1 = new Comment();
        comment1.setText("Comment1Text");
        comment1.setTicket(ticket);
        comment1.setUser(user);
        Comment comment2 = new Comment();
        comment2.setText("Comment2Text");
        comment2.setTicket(ticket);
        comment2.setUser(user);
        ticket.getComments().add(comment1);
        ticket.getComments().add(comment2);
        ticketRepository.save(ticket);
        ticketRepository.delete(ticket);
        List<Comment> expected = new ArrayList<>();
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertFalse(ticketRepository.findById(ticket.getId()).isPresent());
        Assertions.assertEquals(expected, actual);
    }
}