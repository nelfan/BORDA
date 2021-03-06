package com.softserve.borda.services;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.TicketRepository;
import com.softserve.borda.services.impl.TicketServiceImpl;
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
class TicketServiceTest {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    BoardColumnService boardColumnService;

    @InjectMocks
    TicketServiceImpl ticketService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketRepository);
    }

    @Test
    void shouldGetTicketById() {
        Ticket expected = new Ticket();
        expected.setId(1L);
        expected.setTitle("ticket");

        when(ticketRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        Ticket actual = ticketService.getTicketById(1L);
        assertEquals(actual, expected);
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket");

        Ticket expected = new Ticket();
        expected.setId(1L);
        expected.setTitle("ticket");

        when(ticketRepository.save(ticket)).thenReturn(expected);

        Ticket actual = ticketService.create(ticket);

        assertEquals(expected, actual);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void shouldUpdateTicket() {
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket");

        Ticket ticketSaved = new Ticket();
        ticketSaved.setId(1L);
        ticketSaved.setTitle("ticket");

        Ticket ticketUpdated = new Ticket();
        ticketUpdated.setId(1L);
        ticketUpdated.setTitle("ticketUpdated");

        when(ticketRepository.save(ticket)).thenReturn(ticketSaved);

        when(ticketRepository.save(ticketUpdated)).thenReturn(ticketUpdated);

        ticketService.create(ticket);

        Ticket actual = ticketService.update(ticketUpdated);

        assertEquals(ticketUpdated, actual);
        verify(ticketRepository, times(1)).save(ticket);
        verify(ticketRepository, times(1)).save(ticketUpdated);
    }

    @Test
    void shouldDeleteTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("ticket");

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ticketService.deleteTicketById(ticket.getId());

        assertThrows(CustomEntityNotFoundException.class, () -> ticketService.getTicketById(1L));
        verify(ticketRepository, times(1)).deleteById(ticket.getId());
    }


    @Test
    void shouldGetAllTicketsByBoardColumnId() {
        BoardColumn boardColumn = new BoardColumn();
        for (int i = 0; i < 3; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setTitle("ticket" + i);
            boardColumn.getTickets().add(ticket);
        }

        when(boardColumnService.getBoardColumnById(1L)).thenReturn(boardColumn);

        List<Ticket> ticketList = ticketService.getAllTicketsByBoardColumnId(1L);

        assertEquals(3, ticketList.size());
        verify(boardColumnService, times(1)).getBoardColumnById(1L);
    }
}
