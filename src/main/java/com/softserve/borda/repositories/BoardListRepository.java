package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BoardListRepository extends JpaRepository<BoardList, Long> {
}