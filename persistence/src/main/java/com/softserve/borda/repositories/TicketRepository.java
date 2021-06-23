package com.softserve.borda.repositories;

import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> getAllTicketsByBoardColumnId(Long boardColumnId);

    @Modifying
    @Query(value = "insert into tickets_members values(:ticketId, :userId) except " +
            "(select * from tickets_members t where t.user_id = :userId and t.ticket_id = :ticketId)",
            nativeQuery = true)
    Integer createTicketMemberRow(@Param("ticketId") Long ticketId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "delete from tickets_members ticket_member where ticket_member.ticket_id = :ticketId " +
            "and ticket_member.user_id = :userId",
            nativeQuery = true)
    Integer deleteTicketMemberRow(@Param("ticketId") Long ticketId, @Param("userId") Long userId);

    @Query(value = "select t from tickets t join t.tags tg where t.boardColumnId in (select b.id from board_columns b where b.boardId = :board_id) and tg.id in (:tags_id)")
    List<Ticket> getAllTicketsByTags(@Param("tags_id") Long[] tags_id, @Param("board_id") Long board_id);

    @Query(value = "select t from tickets t where t.tags IS EMPTY and boardColumnId in (select b.id from board_columns b where b.boardId = :board_id)")
    List<Ticket> getAllTicketsByNoTags(@Param("board_id") Long board_id);

}
