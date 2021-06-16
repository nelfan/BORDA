package com.softserve.borda.services.impl;

import com.softserve.borda.entities.BoardColumn;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.BoardColumnRepository;
import com.softserve.borda.services.BoardColumnService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;

    @Override
    public BoardColumn getBoardColumnById(Long id) {
        return boardColumnRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardColumn.class));
    }

    @Override
    public BoardColumn create(BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }

    @Override
    public BoardColumn update(BoardColumn boardColumn) {
        BoardColumn existingBoardColumn = getBoardColumnById(boardColumn.getId());
        existingBoardColumn.setName(boardColumn.getName());
        return boardColumnRepository.save(existingBoardColumn);
    }

    @Override
    public boolean deleteBoardColumnById(Long id) {
        try {
            boardColumnRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(BoardColumn.class);
        }
    }

    @Override
    public List<BoardColumn> getAllBoardColumnsByBoardId(Long boardId) {
        return boardColumnRepository.getAllBoardColumnsByBoardId(boardId);
    }

    @Override
    public BoardColumn moveBoardColumnToBoard(Long boardId, Long boardColumnId) {
        BoardColumn boardColumn = getBoardColumnById(boardColumnId);
        boardColumn.setBoardId(boardId);
        return update(boardColumn);
    }

}
