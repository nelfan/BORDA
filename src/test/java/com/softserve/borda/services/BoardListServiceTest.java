package com.softserve.borda.services;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardListRepository;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.impl.BoardListServiceImpl;
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
class BoardListServiceTest {

    @Mock
    BoardRepository boardRepository;

    @Mock
    BoardListRepository boardListRepository;

    @InjectMocks
    BoardListServiceImpl boardListService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        boardListService = new BoardListServiceImpl(boardListRepository, boardRepository);
    }


    @Test
    void shouldGetAllTicketsByBoardListId() {
        BoardList boardList = new BoardList();
        for (int i = 0; i < 3; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setTitle("ticket" + i);
            boardList.getTickets().add(ticket);
        }

        when(boardListRepository.findById(1L)).thenReturn(Optional.of(boardList));

        List<Ticket> ticketList = boardListService.getAllTicketsByBoardListId(1L);

        assertEquals(3, ticketList.size());
        verify(boardListRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetBoardListById() {
        BoardList expected = new BoardList();
        expected.setId(1L);
        expected.setName("boardList");

        when(boardListRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        BoardList actual = boardListService.getBoardListById(1L);
        assertEquals(actual, expected);
        verify(boardListRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("boardList");

        BoardList expected = new BoardList();
        expected.setId(1L);
        expected.setName("boardList");

        when(boardListRepository.save(boardList)).thenReturn(expected);

        BoardList actual = boardListService.createOrUpdate(boardList);

        assertEquals(expected, actual);
        verify(boardListRepository, times(1)).save(boardList);
    }

    @Test
    void shouldUpdateBoardList() {
        BoardList boardList = new BoardList();
        boardList.setName("boardList");

        BoardList boardListSaved = new BoardList();
        boardListSaved.setId(1L);
        boardListSaved.setName("boardList");

        BoardList boardListUpdated = new BoardList();
        boardListUpdated.setId(1L);
        boardListUpdated.setName("boardListUpdated");

        when(boardListRepository.save(boardList)).thenReturn(boardListSaved);

        when(boardListRepository.save(boardListUpdated)).thenReturn(boardListUpdated);

        boardListService.createOrUpdate(boardList);

        BoardList actual = boardListService.createOrUpdate(boardListUpdated);

        assertEquals(boardListUpdated, actual);
        verify(boardListRepository, times(1)).save(boardList);
        verify(boardListRepository, times(1)).save(boardListUpdated);
    }

    @Test
    void shouldDeleteBoardListById() {
        BoardList boardList = new BoardList();
        boardList.setId(1L);
        boardList.setName("boardList");

        when(boardListRepository.findById(1L)).thenReturn(Optional.empty());

        boardListService.deleteBoardListById(boardList.getId());

        assertThrows(CustomEntityNotFoundException.class, () -> boardListService.getBoardListById(1L));
        verify(boardListRepository, times(1)).deleteById(boardList.getId());
    }
}
