package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.impl.BoardServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;

    @Mock
    BoardListService boardListService;

    @Mock
    UserBoardRelationService userBoardRelationService;

    @InjectMocks
    BoardServiceImpl boardService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardServiceImpl(boardRepository,
                userBoardRelationService);
    }

    @Test
    void shouldGetAllBoards() {
        List<Board> boards = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Board board = new Board();
            board.setId((long) i);
            board.setName("board" + i);
            boards.add(board);
        }

        when(boardRepository.findAll()).thenReturn(boards);

        List<Board> boardList = boardService.getAll();

        assertEquals(3, boardList.size());
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    void shouldGetBoardById() {
        Board expected = new Board();
        expected.setId(1L);
        expected.setName("board");

        when(boardRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        Board actual = boardService.getBoardById(1L);
        assertEquals(actual, expected);
        verify(boardRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateBoard() {
        Board board = new Board();
        board.setName("board");

        Board expected = new Board();
        expected.setId(1L);
        expected.setName("board");

        when(boardRepository.save(board)).thenReturn(expected);

        Board actual = boardService.create(board);

        assertEquals(expected, actual);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void shouldUpdateBoard() {
        Board board = new Board();
        board.setName("board");

        Board boardSaved = new Board();
        boardSaved.setId(1L);
        boardSaved.setName("board");

        Board boardUpdated = new Board();
        boardUpdated.setId(1L);
        boardUpdated.setName("boardUpdated");

        when(boardRepository.save(board)).thenReturn(boardSaved);

        when(boardRepository.save(boardUpdated)).thenReturn(boardUpdated);

        boardService.create(board);

        Board actual = boardService.update(boardUpdated);

        assertEquals(boardUpdated, actual);
        verify(boardRepository, times(1)).save(board);
        verify(boardRepository, times(1)).save(boardUpdated);
    }

    @Test
    void shouldDeleteBoardById() {
        Board board = new Board();
        board.setId(1L);
        board.setName("board");

        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        boardService.deleteBoardById(board.getId());

        assertThrows(CustomEntityNotFoundException.class, () -> boardService.getBoardById(1L));
        verify(boardRepository, times(1)).deleteById(board.getId());
    }
}
