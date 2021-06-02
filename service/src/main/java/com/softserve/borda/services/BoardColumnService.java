package com.softserve.borda.services;

import com.softserve.borda.entities.BoardColumn;

import java.util.List;

public interface BoardColumnService {

    BoardColumn getBoardListById(Long id);

    BoardColumn create(BoardColumn boardColumn);

    BoardColumn update(BoardColumn boardColumn);

    boolean deleteBoardListById(Long id);

    List<BoardColumn> getAllBoardListsByBoardId(Long boardId);

    BoardColumn addBoardListToBoard(Long boardId, Long boardListId);

    boolean deleteBoardListFromBoard(Long boardId, Long boardListId);
}
