package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findAllByBoardIdOrderByPositionIndex(Long boardId);

    @Modifying
    @Query("update board_columns board_column set board_column.boardId = :boardId where board_column.id = :boardColumnId")
    Integer updateBoardIdForBoardColumn(@Param("boardColumnId") Long boardColumnId, @Param("boardId") Long boardId);

    boolean existsBoardColumnByIdAndBoardId(Long id, Long boardId);
}