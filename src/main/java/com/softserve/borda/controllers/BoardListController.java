package com.softserve.borda.controllers;

import com.softserve.borda.dto.BoardListDTO;
import com.softserve.borda.dto.TicketDTO;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boardLists")
@AllArgsConstructor
@Log
public class BoardListController {

    private final ModelMapper modelMapper;

    private final BoardListService boardListService;

    private final TicketService ticketService;

    @GetMapping("/{boardListId}")
    public ResponseEntity<BoardListDTO> getBoardListById(@PathVariable Long boardListId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.getBoardListById(boardListId),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{boardListId}")
    public ResponseEntity<BoardListDTO> updateBoardList(@PathVariable Long boardListId,
                                                        @RequestBody BoardListDTO boardList) {
        try {
            BoardList existingBoardList = boardListService.getBoardListById(boardListId);
            BeanUtils.copyProperties(boardList, existingBoardList);
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.createOrUpdate(existingBoardList),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{boardListId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForBoardList(@PathVariable Long boardListId) {
        try {
            return new ResponseEntity<>(
                    boardListService.getAllTicketsByBoardListId(boardListId)
                            .stream().map(ticket -> modelMapper.map(ticket,
                            TicketDTO.class)).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{boardListId}/addTicket")
    public ResponseEntity<BoardListDTO> createTicketForBoardList(@PathVariable long boardListId,
                                                                 @RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
            ticket = ticketService.createOrUpdate(ticket);
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.addTicketToBoardList(
                                    boardListService.getBoardListById(boardListId), ticket),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{boardListId}/tickets/{ticketId}")
    public ResponseEntity<BoardListDTO> deleteTicketFromBoardList(@PathVariable Long boardListId,
                                                                  @PathVariable Long ticketId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.deleteTicketFromBoardList(
                                    boardListService.getBoardListById(boardListId),
                                    ticketService.getTicketById(ticketId)),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(modelMapper.map(
                    boardListService.getBoardListById(boardListId),
                    BoardListDTO.class),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/moveFrom/{oldBoardListId}/to/{newBoardListId}/ticket/{ticketId}/")
    public ResponseEntity<BoardListDTO> moveTicketToAnotherBoardList(@PathVariable Long oldBoardListId,
                                                                  @PathVariable Long newBoardListId,
                                                                  @PathVariable Long ticketId) {
        try {
            BoardList oldBoardList = boardListService.getBoardListById(oldBoardListId);
            BoardList newBoardList = boardListService.getBoardListById(newBoardListId);
            Ticket ticket = ticketService.getTicketById(ticketId);
            oldBoardList.getTickets().remove(ticket);
            newBoardList.getTickets().add(ticket);
            boardListService.createOrUpdate(oldBoardList);
            boardListService.createOrUpdate(newBoardList);
            return new ResponseEntity<>(
                    modelMapper.map(
                            newBoardList,
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
