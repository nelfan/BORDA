package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> getAllBoardColumnsByBoardId(Long boardId);
    boolean existsBoardColumnByIdAndBoardId(Long id, Long boardId);
}