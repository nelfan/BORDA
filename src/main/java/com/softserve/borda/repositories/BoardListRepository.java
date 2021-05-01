package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    List<BoardList> getAllBoardListsByBoardId(Long boardId);
}
