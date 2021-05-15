package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;

import java.util.List;

public interface BoardService {
    List<Board> getAll();
    Board getBoardById(Long id);
    Board createOrUpdate(Board board);
    void deleteBoardById(Long id);

    List<BoardList> getAllBoardListsByBoardId(Long boardId);

    BoardList addBoardListToBoard(Board board, BoardList boardList);
}
