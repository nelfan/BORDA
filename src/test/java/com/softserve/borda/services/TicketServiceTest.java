package com.softserve.borda.services;

import com.softserve.borda.entities.Ticket;
import com.softserve.borda.repositories.BoardListRepository;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class TicketServiceTest {

    @Mock
    BoardListRepository boardListRepository;

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketServiceImpl ticketService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketRepository, boardListRepository);
    }

    @Test
    void shouldGetAllTicketsByBoardListId() {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setName("ticket" + i);
            tickets.add(ticket);
        }

        when(ticketRepository.getAllTicketsByBoardListId(1L)).thenReturn(tickets);

        List<Ticket> ticketList = ticketService.getAllTicketsByBoardListId(1L);

        assertEquals(3, ticketList.size());
        verify(ticketRepository, times(1)).getAllTicketsByBoardListId(1L);
    }

    @Test
    void shouldGetTicketById() {
        Ticket expected = new Ticket();
        expected.setId(1L);
        expected.setName("ticket");

        when(ticketRepository.findById(1L)).thenReturn(java.util.Optional.of(expected));
        Ticket actual = ticketService.getTicketById(1L);
        assertEquals(actual, expected);
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("ticket");

        Ticket expected = new Ticket();
        expected.setId(1L);
        expected.setName("ticket");

        when(ticketRepository.save(ticket)).thenReturn(expected);

        Ticket actual = ticketService.createOrUpdate(ticket);

        assertEquals(expected, actual);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void shouldUpdateTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("ticket");

        Ticket ticketSaved = new Ticket();
        ticketSaved.setId(1L);
        ticketSaved.setName("ticket");

        Ticket ticketUpdated = new Ticket();
        ticketUpdated.setId(1L);
        ticketUpdated.setName("ticketUpdated");

        when(ticketRepository.save(ticket)).thenReturn(ticketSaved);

        when(ticketRepository.save(ticketUpdated)).thenReturn(ticketUpdated);

        ticketService.createOrUpdate(ticket);

        Ticket actual = ticketService.createOrUpdate(ticketUpdated);

        assertEquals(ticketUpdated, actual);
        verify(ticketRepository, times(1)).save(ticket);
        verify(ticketRepository, times(1)).save(ticketUpdated);
    }

    @Test
    void shouldDeleteTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setName("ticket");

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ticketService.deleteTicketById(ticket.getId());

        assertNull(ticketService.getTicketById(1L));
        verify(ticketRepository, times(1)).deleteById(ticket.getId());
    }
}
