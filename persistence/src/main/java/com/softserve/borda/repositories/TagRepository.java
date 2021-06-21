package com.softserve.borda.repositories;

import com.softserve.borda.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllTagsByBoardId(Long boardId);

    @Query(value = "select t from tags t join t.tickets tickets where tickets.id = :ticketId")
    List<Tag> findAllTagsByTicketId(@Param("ticketId") Long ticketId);

    @Modifying
    @Query(value = "insert into tickets_tags values(:ticketId, :tagId) except " +
            "(select * from tickets_tags t WHERE t.tag_id = :tagId and t.ticket_id = :ticketId)",
            nativeQuery = true)
    Integer createTagTicketRow(@Param("ticketId") Long ticketId, @Param("tagId") Long tagId);

    @Modifying
    @Query(value = "delete from tickets_tags ticket_tag where ticket_tag.ticket_id = :ticketId " +
            "and ticket_tag.tag_id = :tagId",
            nativeQuery = true)
    Integer deleteTagTicketRow(@Param("ticketId") Long ticketId, @Param("tagId") Long tagId);
}
