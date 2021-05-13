package com.softserve.borda.controllers;

import com.softserve.borda.dto.CreateBoardDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.UserBoardRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    private final UserBoardRelationService userBoardRelationService;

    public BoardController(BoardService boardService, UserBoardRelationService userBoardRelationService) {
        this.boardService = boardService;
        this.userBoardRelationService = userBoardRelationService;
    }

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

        userBoardRelation.getBoardRoles().add(userBoardRelationService
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
}