package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface BoardListService {
    List<BoardList> getAllBoardListsByBoardId(Long boardId);
    BoardList getBoardListById(Long id);
    BoardList createOrUpdate(BoardList boardList);
    void deleteBoardListById(Long id);
    boolean addBoardListToBoard(BoardList boardList, @NotNull Board board);
}
