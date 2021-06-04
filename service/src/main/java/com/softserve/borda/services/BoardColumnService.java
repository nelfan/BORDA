package com.softserve.borda.services;

import com.softserve.borda.entities.BoardColumn;

import java.util.List;

public interface BoardColumnService {

    BoardColumn getBoardColumnById(Long id);

    BoardColumn create(BoardColumn boardColumn);

    BoardColumn update(BoardColumn boardColumn);

    boolean deleteBoardColumnById(Long id);

    List<BoardColumn> getAllBoardColumnsByBoardId(Long boardId);

    BoardColumn addBoardColumnToBoard(Long boardId, Long boardColumnId);

    boolean deleteBoardColumnFromBoard(Long boardId, Long boardColumnId);
}