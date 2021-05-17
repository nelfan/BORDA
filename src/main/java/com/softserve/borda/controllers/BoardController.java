package com.softserve.borda.controllers;

import com.softserve.borda.dto.BoardListDTO;
import com.softserve.borda.dto.CreateBoardDTO;
import com.softserve.borda.dto.TicketDTO;
import com.softserve.borda.entities.*;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.TicketService;
import com.softserve.borda.services.UserBoardRelationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardListService boardListService;

    private final UserBoardRelationService userBoardRelationService;

    private final TicketService ticketService;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @GetMapping("{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @PostMapping
    public Board createBoard(@RequestBody final CreateBoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setBoard(board);
        //TODO: get user ID from session
        userBoardRelation.setUser(User.builder().id(1L).build());

        userBoardRelation.setBoardRole(userBoardRelationService
                .getBoardRoleByName(BoardRole.BoardRoles.OWNER.name()));

        board.setUserBoardRelations(Collections.singletonList(userBoardRelation));

        return boardService.createOrUpdate(board);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id) {
        boardService.deleteBoardById(id);
    }

    @PutMapping(value = "{id}")
    public Board update(@PathVariable Long id, @RequestBody Board board) {
        Board existingBoard = boardService.getBoardById(id);
        BeanUtils.copyProperties(board, existingBoard);
        return boardService.createOrUpdate(existingBoard);
    }

    @GetMapping("{id}/boardLists")
    public List<BoardList> getAllBoardListsForBoard(@PathVariable Long id) {
        return boardService.getAllBoardListsByBoardId(id);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}")
    public BoardList getBoardListForBoardById(@PathVariable Long boardId, @PathVariable Long boardListId) {
        boardService.getBoardById(boardId);
        return boardListService.getBoardListById(boardListId);
    }

    @PostMapping("{boardId}/addBoardList")
    public BoardList createBoardListsForBoard(@PathVariable Long boardId,
                                              @RequestBody BoardListDTO boardListDTO) {
        BoardList boardList = new BoardList();
        boardList.setName(boardListDTO.getName());
        boardList = boardListService.createOrUpdate(boardList);
        return boardService.addBoardListToBoard(boardService.getBoardById(boardId), boardList);
    }

    @DeleteMapping(value = "{boardId}/boardLists/{boardListId}")
    public void deleteBoardList(@PathVariable Long boardId, @PathVariable Long boardListId) {
        boardService.getBoardById(boardId);
        boardListService.deleteBoardListById(boardListId);
    }

    @PutMapping(value = "{boardId}/boardLists/{boardListId}")
    public BoardList updateBoardList(@PathVariable Long boardId, @PathVariable Long boardListId,
                                     @RequestBody BoardList boardList) {
        boardService.getBoardById(boardId);
        BoardList existingBoardList = boardListService.getBoardListById(boardListId);
        BeanUtils.copyProperties(boardList, existingBoardList);
        return boardListService.createOrUpdate(existingBoardList);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}/tickets")
    public List<Ticket> getAllTicketsForBoardList(@PathVariable Long boardId, @PathVariable Long boardListId) {
        boardService.getBoardById(boardId);
        return boardListService.getAllTicketsByBoardListId(boardListId);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}/tickets/{ticketId}")
    public Ticket getTicketForBoardListById(@PathVariable Long boardId, @PathVariable Long boardListId
            , @PathVariable Long ticketId) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        return ticketService.getTicketById(ticketId);
    }

    @PostMapping("{boardId}/boardLists/{boardListId}/addTicket")
    public BoardList createTicketForBoardList(@PathVariable long boardId, @PathVariable long boardListId,
                                              @RequestBody TicketDTO ticketDTO) {
        boardService.getBoardById(boardId);
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setImg(ticketDTO.getImg());
        ticket = ticketService.createOrUpdate(ticket);
        return boardListService.addTicketToBoardList(boardListService.getBoardListById(boardListId), ticket);
    }

    @DeleteMapping(value = "{boardId}/boardLists/{boardListId}/tickets/{ticketId}")
    public void deleteTicket(@PathVariable Long boardId, @PathVariable Long boardListId
            , @PathVariable Long ticketId) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        ticketService.deleteTicketById(ticketId);
    }

    @PutMapping(value = "{boardId}/boardLists/{boardListId}/tickets/{ticketId}")
    public Ticket updateTicket(@PathVariable Long boardId, @PathVariable Long boardListId
            , @PathVariable Long ticketId, @RequestBody Ticket ticket) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        Ticket existingTicket = ticketService.getTicketById(ticketId);
        BeanUtils.copyProperties(ticket, existingTicket);
        return ticketService.createOrUpdate(existingTicket);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}/tickets/{ticketId}/comments")
    public List<Comment> getAllCommentsForTicket(@PathVariable Long boardId, @PathVariable Long boardListId,
                                                 @PathVariable Long ticketId) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        ticketService.getTicketById(ticketId);
        return ticketService.getAllCommentsByTicketId(ticketId);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}/tickets/{ticketId}/tags")
    public List<Tag> getAllTagsForTicket(@PathVariable Long boardId, @PathVariable Long boardListId,
                                         @PathVariable Long ticketId) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        ticketService.getTicketById(ticketId);
        return ticketService.getAllTagsByTicketId(ticketId);
    }

    @GetMapping("{boardId}/boardLists/{boardListId}/tickets/{ticketId}/members")
    public List<User> getAllMembersForTicket(@PathVariable Long boardId, @PathVariable Long boardListId,
                                             @PathVariable Long ticketId) {
        boardService.getBoardById(boardId);
        boardListService.getBoardListById(boardListId);
        ticketService.getTicketById(ticketId);
        return ticketService.getAllMembersByTicketId(ticketId);
    }

}