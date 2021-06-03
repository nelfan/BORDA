package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardColumn;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardColumnRepository;
import com.softserve.borda.services.impl.BoardColumnServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BoardBoardColumnServiceTest {

    @Mock
    BoardService boardService;

    @Mock
    BoardColumnRepository boardColumnRepository;

    @InjectMocks
    BoardColumnServiceImpl boardColumnService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        boardColumnService = new BoardColumnServiceImpl(boardColumnRepository, boardService);
    }

    @Test
    void shouldGetBoardListById() {
        BoardColumn expected = new BoardColumn();
        expected.setId(1L);
        expected.setName("boardColumn");

        when(boardColumnRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        BoardColumn actual = boardColumnService.getBoardColumnById(1L);
        assertEquals(actual, expected);
        verify(boardColumnRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateBoardList() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("boardColumn");

        BoardColumn expected = new BoardColumn();
        expected.setId(1L);
        expected.setName("boardColumn");

        when(boardColumnRepository.save(boardColumn)).thenReturn(expected);

        BoardColumn actual = boardColumnService.create(boardColumn);

        assertEquals(expected, actual);
        verify(boardColumnRepository, times(1)).save(boardColumn);
    }

    @Test
    void shouldUpdateBoardList() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName("boardColumn");

        BoardColumn boardColumnSaved = new BoardColumn();
        boardColumnSaved.setId(1L);
        boardColumnSaved.setName("boardColumn");

        BoardColumn boardColumnUpdated = new BoardColumn();
        boardColumnUpdated.setId(1L);
        boardColumnUpdated.setName("boardColumnUpdated");

        when(boardColumnRepository.save(boardColumn)).thenReturn(boardColumnSaved);

        when(boardColumnRepository.save(boardColumnUpdated)).thenReturn(boardColumnUpdated);

        boardColumnService.create(boardColumn);

        BoardColumn actual = boardColumnService.update(boardColumnUpdated);

        assertEquals(boardColumnUpdated, actual);
        verify(boardColumnRepository, times(1)).save(boardColumn);
        verify(boardColumnRepository, times(1)).save(boardColumnUpdated);
    }

    @Test
    void shouldDeleteBoardListById() {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(1L);
        boardColumn.setName("boardColumn");

        when(boardColumnRepository.findById(1L)).thenReturn(Optional.empty());

        boardColumnService.deleteBoardColumnById(boardColumn.getId());

        assertThrows(CustomEntityNotFoundException.class, () -> boardColumnService.getBoardColumnById(1L));
        verify(boardColumnRepository, times(1)).deleteById(boardColumn.getId());
    }


    @Test
    void shouldGetAllBoardListsByBoardId() {
        Board board = new Board();
        for (int i = 0; i < 3; i++) {
            BoardColumn boardColumn = new BoardColumn();
            boardColumn.setId((long) i);
            boardColumn.setName("boardColumn" + i);
            board.getBoardColumns().add(boardColumn);
        }

        when(boardService.getBoardById(1L)).thenReturn(board);

        List<BoardColumn> boardColumns = boardColumnService.getAllBoardColumnsByBoardId(1L);

        assertEquals(3, boardColumns.size());
        verify(boardService, times(1)).getBoardById(1L);
    }
}
