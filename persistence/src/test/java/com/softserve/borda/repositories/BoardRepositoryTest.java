package com.softserve.borda.repositories;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardColumn;
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
    BoardColumnRepository boardColumnRepository;

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
    void shouldSaveBoardColumnsWithBoard() {
        Board board = new Board();
        board.setName("Board1");
        BoardColumn boardColumn1 = new BoardColumn();
        boardColumn1.setName("BoardColumn1");
        BoardColumn boardColumn2 = new BoardColumn();
        boardColumn2.setName("BoardColumn2");
        board.getBoardColumns().add(boardColumn1);
        board.getBoardColumns().add(boardColumn2);
        boardRepository.save(board);
        Board actualBoard = boardRepository.getOne(board.getId());
        List<BoardColumn> expected = new ArrayList<>();
        expected.add(boardColumn1);
        expected.add(boardColumn2);
        List<BoardColumn> actual = boardColumnRepository.findAll();
        Assertions.assertEquals(board, actualBoard);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteBoardColumnsWithBoard() {
        Board board = new Board();
        board.setName("Board1");
        BoardColumn boardColumn1 = new BoardColumn();
        boardColumn1.setName("BoardColumn1");
        BoardColumn boardColumn2 = new BoardColumn();
        boardColumn2.setName("BoardColumn2");
        board.getBoardColumns().add(boardColumn1);
        board.getBoardColumns().add(boardColumn2);
        boardRepository.save(board);
        boardRepository.delete(board);
        List<BoardColumn> expected = new ArrayList<>();
        List<BoardColumn> actual = boardColumnRepository.findAll();
        Assertions.assertFalse(boardRepository.findById(board.getId()).isPresent());
        Assertions.assertEquals(expected, actual);
    }
}