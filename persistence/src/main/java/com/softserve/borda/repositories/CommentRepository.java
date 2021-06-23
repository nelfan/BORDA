package com.softserve.borda.repositories;

import com.softserve.borda.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getAllCommentsByTicketId(Long ticketId);

    boolean existsCommentByIdAndTicketId(Long id, Long ticketId);
}
