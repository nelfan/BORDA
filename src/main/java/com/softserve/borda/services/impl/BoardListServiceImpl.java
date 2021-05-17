package com.softserve.borda.services.impl;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardListRepository;
import com.softserve.borda.repositories.BoardRepository;
import com.softserve.borda.services.BoardListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardListServiceImpl implements BoardListService {

    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;

    @Override
    public List<Ticket> getAllTicketsByBoardListId(Long boardListId) {
        return getBoardListById(boardListId).getTickets();
    }

    @Override
    public BoardList addTicketToBoardList(BoardList boardList, Ticket ticket) {
        boardList.getTickets().add(ticket);
        return boardListRepository.save(boardList);
    }

    @Override
    public BoardList getBoardListById(Long id) {
        return boardListRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardList.class));
    }

    @Override
    public BoardList createOrUpdate(BoardList boardList) {
        if (boardList.getId() != null) {
            Optional<BoardList> boardListOptional = boardListRepository.findById(boardList.getId());

            if (boardListOptional.isPresent()) {
                BoardList newBoardList = boardListOptional.get();
                newBoardList.setName(boardList.getName());
                return boardListRepository.save(newBoardList);
            }
        }
        return boardListRepository.save(boardList);
    }

    @Override
    public void deleteBoardListById(Long id) {
        boardListRepository.deleteById(id);
    }
}
