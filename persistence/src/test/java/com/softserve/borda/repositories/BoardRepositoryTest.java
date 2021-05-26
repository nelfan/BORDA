package com.softserve.borda.repositories;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import org.junit.jupiter.api.Assertions;
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
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardListRepository boardListRepository;

    @Test
    void shouldInsertAndReturnBoard() {
        Board board = new Board();
        board.setName("Board1");
        Board expected = boardRepository.save(board);
        Board actual = boardRepository.getOne(board.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteBoard() {
        Board board = new Board();
        board.setName("Board1");
        boardRepository.save(board);
        boardRepository.deleteById(board.getId());
        Assertions.assertFalse(boardRepository.findById(board.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateBoard() {
        Board board = new Board();
        board.setName("Board1");
        boardRepository.save(board);
        board.setName("Board1updated");
        Board expected = boardRepository.save(board);
        Board actual = boardRepository.getOne(board.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldSaveBoardListsWithBoard() {
        Board board = new Board();
        board.setName("Board1");
        BoardList boardList1 = new BoardList();
        boardList1.setName("BoardList1");
        BoardList boardList2 = new BoardList();
        boardList2.setName("BoardList2");
        board.getBoardLists().add(boardList1);
        board.getBoardLists().add(boardList2);
        boardRepository.save(board);
        Board actualBoard = boardRepository.getOne(board.getId());
        List<BoardList> expected = new ArrayList<>();
        expected.add(boardList1);
        expected.add(boardList2);
        List<BoardList> actual = boardListRepository.findAll();
        Assertions.assertEquals(board, actualBoard);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteBoardListsWithBoard() {
        Board board = new Board();
        board.setName("Board1");
        BoardList boardList1 = new BoardList();
        boardList1.setName("BoardList1");
        BoardList boardList2 = new BoardList();
        boardList2.setName("BoardList2");
        board.getBoardLists().add(boardList1);
        board.getBoardLists().add(boardList2);
        boardRepository.save(board);
        boardRepository.delete(board);
        List<BoardList> expected = new ArrayList<>();
        List<BoardList> actual = boardListRepository.findAll();
        Assertions.assertFalse(boardRepository.findById(board.getId()).isPresent());
        Assertions.assertEquals(expected, actual);
    }
}