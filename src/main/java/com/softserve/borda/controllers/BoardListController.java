package com.softserve.borda.controllers;

import com.softserve.borda.dto.TicketDTO;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardLists")
@AllArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    private final TicketService ticketService;

    @GetMapping("{boardListId}")
    public BoardList getBoardListById(@PathVariable Long boardListId) {
        return boardListService.getBoardListById(boardListId);
    }

    @PutMapping(value = "{boardListId}")
    public BoardList updateBoardList(@PathVariable Long boardListId,
                                     BoardList boardList) {
        BoardList existingBoardList = boardListService.getBoardListById(boardListId);
        BeanUtils.copyProperties(boardList, existingBoardList);
        return boardListService.createOrUpdate(existingBoardList);
    }

    @GetMapping("{boardListId}/tickets")
    public List<Ticket> getAllTicketsForBoardList(@PathVariable Long boardListId) {
        return boardListService.getAllTicketsByBoardListId(boardListId);
    }

    @PostMapping("{boardListId}/addTicket")
    public BoardList createTicketForBoardList(@PathVariable long boardListId,
                                              TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setImg(ticketDTO.getImg());
        ticket = ticketService.createOrUpdate(ticket);
        return boardListService.addTicketToBoardList(boardListService.getBoardListById(boardListId), ticket);
    }

    @DeleteMapping(value = "{boardListId}/tickets/{ticketId}")
    public ResponseEntity<String> deleteTicketFromBoardList(@PathVariable Long boardListId,
                                               @PathVariable Long ticketId) {
        try {
            boardListService.deleteTicketFromBoardList(boardListService.getBoardListById(boardListId),
                    ticketService.getTicketById(ticketId));
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            return new ResponseEntity<>("Failed to delete ticket with Id: " + ticketId,
                    HttpStatus.NOT_FOUND);
        }
    }
}
