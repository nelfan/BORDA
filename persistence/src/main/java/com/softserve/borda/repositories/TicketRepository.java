package com.softserve.borda.repositories;

import com.softserve.borda.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);
    boolean existsTicketByIdAndBoardColumnId(Long id, Long boardColumnId);
}
