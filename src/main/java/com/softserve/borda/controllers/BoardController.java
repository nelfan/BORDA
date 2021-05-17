package com.softserve.borda.controllers;

import com.softserve.borda.dto.BoardListDTO;
import com.softserve.borda.dto.CreateBoardDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final UserService userService;

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

    @PostMapping("createBoard/{userId}")
    public Board createBoard(@PathVariable Long userId, CreateBoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setBoard(board);
        userBoardRelation.setUser(userService.getUserById(userId));

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
    public List<BoardList> getAllBoardListsForBoard(@PathVariable Long id) {
        return boardService.getAllBoardListsByBoardId(id);
    }

    @PostMapping("{boardId}/addBoardList")
    public BoardList createBoardListsForBoard(@PathVariable Long boardId,
                                              BoardListDTO boardListDTO) {
        BoardList boardList = new BoardList();
        boardList.setName(boardListDTO.getName());
        boardList = boardListService.createOrUpdate(boardList);
        return boardService.addBoardListToBoard(boardService.getBoardById(boardId), boardList);
    }

    @DeleteMapping(value = "{boardId}/boardLists/{boardListId}")
    public ResponseEntity<String> deleteBoardListFromBoard(@PathVariable Long boardId,
                                                           @PathVariable Long boardListId) {
        try {
            boardService.deleteBoardListFromBoard(boardService.getBoardById(boardId),
                    boardListService.getBoardListById(boardListId));
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            return new ResponseEntity<>("Failed to delete boardList with Id: " + boardListId,
                    HttpStatus.NOT_FOUND);
        }
    }
}