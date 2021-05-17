package com.softserve.borda.services;

import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface BoardListService {
    BoardList getBoardListById(Long id);
    BoardList createOrUpdate(BoardList boardList);
    void deleteBoardListById(Long id);

    List<Ticket> getAllTicketsByBoardListId(Long boardListId);

    BoardList addTicketToBoardList(@NotNull BoardList boardList, Ticket ticket);
}
