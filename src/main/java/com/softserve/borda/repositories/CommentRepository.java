package com.softserve.borda.repositories;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Tag> getAllCommentsByTicketId(Long ticketId);
}
