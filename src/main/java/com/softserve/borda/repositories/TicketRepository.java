package com.softserve.borda.repositories;

import com.softserve.borda.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> getAllTicketsByBoardListId(Long boardListId);
}
