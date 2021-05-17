package com.softserve.borda.controllers;

import com.softserve.borda.dto.BoardListDTO;
import com.softserve.borda.dto.CreateBoardDTO;
import com.softserve.borda.entities.*;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.UserBoardRelationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardListService boardListService;

    private final UserBoardRelationService userBoardRelationService;

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
    public Board update(@PathVariable Long id, Board board) {
        Board existingBoard = boardService.getBoardById(id);
        BeanUtils.copyProperties(board, existingBoard);
        return boardService.createOrUpdate(existingBoard);
    }

    @GetMapping("{id}/boardLists")
    public List<BoardList> getBoardListsForBoard(@PathVariable Long id) {
        return boardService.getAllBoardListsByBoardId(id);
    }

    @PostMapping("{id}/addBoardList")
    public BoardList createBoardListsForBoard(@PathVariable Long id,
                                                    BoardListDTO boardListDTO) {
        BoardList boardList = new BoardList();
        boardList.setName(boardListDTO.getName());
        boardList = boardListService.createOrUpdate(boardList);
        return boardService.addBoardListToBoard(boardService.getBoardById(id), boardList);
    }
}