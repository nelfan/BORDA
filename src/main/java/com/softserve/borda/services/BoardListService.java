package com.softserve.borda.services;

import com.softserve.borda.entities.BoardList;

import java.util.List;

public interface BoardListService {
    List<BoardList> getAll();
    BoardList getBoardListById(Long id);
    BoardList createOrUpdate(BoardList boardList);
    void deleteBoardListById(Long id);
}
