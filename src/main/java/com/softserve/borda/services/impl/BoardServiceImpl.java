package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.BoardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @Override
    public Board getBoardById(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if(board.isPresent()) {
            return board.get();
        }
        return null; // TODO: Throw custom exception
    }

    @Override
    public Board createOrUpdate(Board board) {
        if (board.getId() != null) {
            Optional<Board> boardOptional = boardRepository.findById(board.getId());

            if (boardOptional.isPresent()) {
                Board newBoard = boardOptional.get();
                newBoard.setName(board.getName());
                return boardRepository.save(newBoard);
            }
        }
        return boardRepository.save(board);
    }

    @Override
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }
}
