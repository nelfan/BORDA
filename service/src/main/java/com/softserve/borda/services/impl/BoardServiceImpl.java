package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.TagService;
import com.softserve.borda.services.UserBoardRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final UserBoardRelationService userBoardRelationService;

    @Override
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @Override
    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(Board.class));
    }

    @Override
    public Board create(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board update(Board board) {
        Board existingBoard = getBoardById(board.getId());
        existingBoard.setName(board.getName());
        return boardRepository.save(existingBoard);
    }

    @Override
    public boolean deleteBoardById(Long id) {
        try {
            boardRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Board> getBoardsByUserId(Long userId) {
        return userBoardRelationService.getUserBoardRelationsByUserId(userId).stream()
                .map(UserBoardRelation::getBoard)
                .collect(Collectors.toList());
    }

    @Override
    public List<Board> getBoardsByUserIdAndBoardRoleId(Long userId, Long roleId) {
        return userBoardRelationService.getUserBoardRelationsByUserIdAndBoardRoleId(userId, roleId)
                .stream()
                .map(UserBoardRelation::getBoard)
                .collect(Collectors.toList());
    }


}
