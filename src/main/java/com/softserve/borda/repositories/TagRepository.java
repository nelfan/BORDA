package com.softserve.borda.repositories;

import com.softserve.borda.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> getAllTagsByTicketId(Long ticketId);
}
