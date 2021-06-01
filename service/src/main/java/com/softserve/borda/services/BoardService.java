package com.softserve.borda.services;

import com.softserve.borda.entities.Board;

import java.util.List;

public interface BoardService {
    List<Board> getAll();

    Board getBoardById(Long id);

    Board create(Board board);

    Board update(Board board);

    boolean deleteBoardById(Long id);

    List<Board> getBoardsByUserId(Long id);

    List<Board> getBoardsByUserIdAndBoardRoleId(Long userId, Long roleId);
}
