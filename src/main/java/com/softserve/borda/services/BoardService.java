package com.softserve.borda.services;

import com.softserve.borda.entities.Board;

import java.util.List;

public interface BoardService {
    List<Board> getAll();
    Board getBoardById(Long id);
    Board createOrUpdate(Board board);
    void deleteBoardById(Long id);
}
