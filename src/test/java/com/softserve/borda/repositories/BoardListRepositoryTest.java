package com.softserve.borda.repositories;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
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
class BoardListRepositoryTest {

    @Autowired
    BoardListRepository boardListRepository;

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
    void shouldInsertAndReturnBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        BoardList expected = boardListRepository.save(boardList);
        BoardList actual = boardListRepository.getOne(boardList.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        boardListRepository.save(boardList);
        boardListRepository.deleteById(boardList.getId());
        Assertions.assertFalse(boardListRepository.findById(boardList.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        boardListRepository.save(boardList);
        boardList.setName("BoardList1updated");
        BoardList expected = boardListRepository.save(boardList);
        BoardList actual = boardListRepository.getOne(boardList.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSaveTicketsWithBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        Ticket ticket1 = new Ticket();
        ticket1.setTitle("Ticket1");
        ticket1.setDescription("Ticket1Body");
        Ticket ticket2 = new Ticket();
        ticket2.setTitle("Ticket2");
        ticket2.setDescription("Ticket2Body");
        boardList.getTickets().add(ticket1);
        boardList.getTickets().add(ticket2);
        boardListRepository.save(boardList);
        BoardList actualBoardList = boardListRepository.getOne(boardList.getId());
        List<Ticket> expected = new ArrayList<>();
        expected.add(ticket1);
        expected.add(ticket2);
        List<Ticket> actual = ticketRepository.findAll();
        Assertions.assertEquals(boardList, actualBoardList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteTicketsWithBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("BoardList1");
        Ticket ticket1 = new Ticket();
        ticket1.setTitle("Ticket1");
        ticket1.setDescription("Ticket1Body");
        Ticket ticket2 = new Ticket();
        ticket2.setTitle("Ticket2");
        ticket2.setDescription("Ticket2Body");
        boardList.getTickets().add(ticket1);
        boardList.getTickets().add(ticket2);
        boardListRepository.save(boardList);
        boardListRepository.delete(boardList);
        List<Ticket> expected = new ArrayList<>();
        List<Ticket> actual = ticketRepository.findAll();
        Assertions.assertFalse(boardListRepository.findById(boardList.getId()).isPresent());
        Assertions.assertEquals(expected, actual);
    }
}