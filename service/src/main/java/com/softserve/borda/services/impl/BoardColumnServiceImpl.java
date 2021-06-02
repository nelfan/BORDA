package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardColumn;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardColumnRepository;
import com.softserve.borda.services.BoardColumnService;
import com.softserve.borda.services.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final BoardService boardService;

    @Override
    public BoardColumn getBoardListById(Long id) {
        return boardColumnRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardColumn.class));
    }

    @Override
    public BoardColumn create(BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }

    @Override
    public BoardColumn update(BoardColumn boardColumn) {
        BoardColumn existingBoardColumn = getBoardListById(boardColumn.getId());
        existingBoardColumn.setName(boardColumn.getName());
        return boardColumnRepository.save(existingBoardColumn);
    }

    @Override
    public boolean deleteBoardListById(Long id) {
        try {
            boardColumnRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public List<BoardColumn> getAllBoardListsByBoardId(Long boardId) {
        return boardService.getBoardById(boardId).getBoardColumns();
    }

    @Override
    public BoardColumn addBoardListToBoard(Long boardId, Long boardListId) {
        Board board = boardService.getBoardById(boardId);
        BoardColumn boardColumn = getBoardListById(boardListId);
        board.getBoardColumns().add(boardColumn);
        boardService.update(board);
        return boardColumn;
    }

    @Override
    public boolean deleteBoardListFromBoard(Long boardId, Long boardListId) {
        try {
            Board board = boardService.getBoardById(boardId);
            BoardColumn boardColumn = getBoardListById(boardListId);
            board.getBoardColumns().remove(boardColumn);
            deleteBoardListById(boardColumn.getId());
            boardService.update(board);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

}
