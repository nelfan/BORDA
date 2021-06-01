package com.softserve.borda.services;

import com.softserve.borda.entities.BoardList;

import java.util.List;

public interface BoardListService {

    BoardList getBoardListById(Long id);
    BoardList create(BoardList boardList);
    BoardList update(BoardList boardList);
    boolean deleteBoardListById(Long id);

    List<BoardList> getAllBoardListsByBoardId(Long boardId);

    BoardList addBoardListToBoard(Long boardId, Long boardListId);

    boolean deleteBoardListFromBoard(Long boardId, Long boardListId);
}
