package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public List<BoardList> getAllBoardListsByBoardId(Long boardId) {
        return getBoardById(boardId).getBoardLists();
    }

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

    @Override
    public BoardList addBoardListToBoard(Board board, BoardList boardList) {
        board.getBoardLists().add(boardList);
        boardRepository.save(board);
        return boardList;
    }
}
