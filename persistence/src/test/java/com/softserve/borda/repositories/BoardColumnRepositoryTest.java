package com.softserve.borda.repositories;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardColumn;
import com.softserve.borda.entities.Ticket;
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
class BoardColumnRepositoryTest {

    @Autowired
    BoardColumnRepository boardColumnRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TicketRepository ticketRepository;

    Board board;

    @BeforeEach
    void boardSetUp() {
        board = new Board();
        board.setName("Board1");
        boardRepository.save(board);
    }

    @Test
    void shouldInsertAndReturnBoardColumn() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        BoardColumn expected = boardColumnRepository.save(boardColumn);
        BoardColumn actual = boardColumnRepository.getOne(boardColumn.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteBoardColumn() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        boardColumnRepository.save(boardColumn);
        boardColumnRepository.deleteById(boardColumn.getId());
        Assertions.assertFalse(boardColumnRepository.findById(boardColumn.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateBoardColumn() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        boardColumnRepository.save(boardColumn);
        boardColumn.setName("BoardColumn1updated");
        BoardColumn expected = boardColumnRepository.save(boardColumn);
        BoardColumn actual = boardColumnRepository.getOne(boardColumn.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSaveTicketsWithBoardColumn() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        Ticket ticket1 = new Ticket();
        ticket1.setTitle("Ticket1");
        ticket1.setDescription("Ticket1Body");
        Ticket ticket2 = new Ticket();
        ticket2.setTitle("Ticket2");
        ticket2.setDescription("Ticket2Body");
        boardColumn.getTickets().add(ticket1);
        boardColumn.getTickets().add(ticket2);
        boardColumnRepository.save(boardColumn);
        BoardColumn actualBoardColumn = boardColumnRepository.getOne(boardColumn.getId());
        List<Ticket> expected = new ArrayList<>();
        expected.add(ticket1);
        expected.add(ticket2);
        List<Ticket> actual = ticketRepository.findAll();
        Assertions.assertEquals(boardColumn, actualBoardColumn);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteTicketsWithBoardColumn() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("BoardColumn1");
        Ticket ticket1 = new Ticket();
        ticket1.setTitle("Ticket1");
        ticket1.setDescription("Ticket1Body");
        Ticket ticket2 = new Ticket();
        ticket2.setTitle("Ticket2");
        ticket2.setDescription("Ticket2Body");
        boardColumn.getTickets().add(ticket1);
        boardColumn.getTickets().add(ticket2);
        boardColumnRepository.save(boardColumn);
        boardColumnRepository.delete(boardColumn);
        List<Ticket> expected = new ArrayList<>();
        List<Ticket> actual = ticketRepository.findAll();
        Assertions.assertFalse(boardColumnRepository.findById(boardColumn.getId()).isPresent());
        Assertions.assertEquals(expected, actual);
    }
}