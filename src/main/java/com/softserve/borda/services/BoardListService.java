package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface BoardListService {
    List<Ticket> getAllTicketsByBoardListId(Long boardListId);
    BoardList getBoardListById(Long id);
    BoardList createOrUpdate(BoardList boardList);
    void deleteBoardListById(Long id);
    boolean addBoardListToBoard(BoardList boardList, @NotNull Board board);
}
