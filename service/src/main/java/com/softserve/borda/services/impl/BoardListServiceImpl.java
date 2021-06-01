package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardListRepository;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class BoardListServiceImpl implements BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardService boardService;

    @Override
    public BoardList getBoardListById(Long id) {
        return boardListRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardList.class));
    }

    @Override
    public BoardList create(BoardList boardList) {
        return boardListRepository.save(boardList);
    }

    @Override
    public BoardList update(BoardList boardList) {
        BoardList existingBoardList = getBoardListById(boardList.getId());
        existingBoardList.setName(boardList.getName());
        return boardListRepository.save(existingBoardList);
    }

    @Override
    public boolean deleteBoardListById(Long id) {
        try {
            boardListRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public List<BoardList> getAllBoardListsByBoardId(Long boardId) {
        return boardService.getBoardById(boardId).getBoardLists();
    }

    @Override
    public BoardList addBoardListToBoard(Long boardId, Long boardListId) {
        Board board = boardService.getBoardById(boardId);
        BoardList boardList = getBoardListById(boardListId);
        board.getBoardLists().add(boardList);
        boardService.update(board);
        return boardList;
    }

    @Override
    public boolean deleteBoardListFromBoard(Long boardId, Long boardListId) {
        try {
            Board board = boardService.getBoardById(boardId);
            BoardList boardList = getBoardListById(boardListId);
            board.getBoardLists().remove(boardList);
            deleteBoardListById(boardList.getId());
            boardService.update(board);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

}
