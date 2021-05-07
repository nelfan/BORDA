package com.softserve.borda.repositories;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long> {
}
