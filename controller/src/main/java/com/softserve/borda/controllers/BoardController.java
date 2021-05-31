package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.BoardFullDTO;
import com.softserve.borda.dto.BoardListDTO;
import com.softserve.borda.dto.CreateBoardDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.BoardList;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.services.BoardListService;
import com.softserve.borda.services.BoardService;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boards")
@AllArgsConstructor
@Log
@CrossOrigin
public class BoardController {

    private final ModelMapper modelMapper;

    private final BoardService boardService;

    private final UserService userService;

    private final BoardListService boardListService;

    private final UserBoardRelationService userBoardRelationService;

    private final JwtConvertor jwtConvertor;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @GetMapping("/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @PostMapping
    public ResponseEntity<BoardFullDTO> createBoard(@RequestBody CreateBoardDTO boardDTO,
                                                    @RequestHeader String authorization) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setBoard(board);
        userBoardRelation.setUser((jwtConvertor.getUserByJWT(authorization)));

        userBoardRelation.setBoardRole(userBoardRelationService
                .getBoardRoleByName(BoardRole.BoardRoles.OWNER.name()));

        board.setUserBoardRelations(Collections.singletonList(userBoardRelation));

        return new ResponseEntity<>(modelMapper.map(
                boardService.createOrUpdate(board),
                BoardFullDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoardById(id);
        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<BoardFullDTO> updateBoard(@PathVariable Long id,
                                                    @RequestBody BoardFullDTO board) {
        Board existingBoard = boardService.getBoardById(id);
        BeanUtils.copyProperties(board, existingBoard);
        return new ResponseEntity<>(modelMapper.map(
                boardService.createOrUpdate(existingBoard),
                BoardFullDTO.class), HttpStatus.OK);
    }

    @GetMapping("{id}/boardLists")
    public ResponseEntity<List<BoardListDTO>> getAllBoardListsForBoard(@PathVariable Long id) {
        return new ResponseEntity<>(
                boardService.getAllBoardListsByBoardId(id)
                        .stream().map(boardList -> modelMapper.map(boardList,
                        BoardListDTO.class)).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping("/{boardId}/addBoardList")
    public ResponseEntity<BoardListDTO> createBoardListsForBoard(@PathVariable Long boardId,
                                                                 @RequestBody BoardListDTO boardListDTO) {
        BoardList boardList = new BoardList();
        boardList.setName(boardListDTO.getName());
        boardList = boardListService.createOrUpdate(boardList);
        return new ResponseEntity<>(
                modelMapper.map(
                        boardService.addBoardListToBoard(
                                boardService.getBoardById(boardId), boardList),
                        BoardListDTO.class),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "{boardId}/boardLists/{boardListId}")
    public ResponseEntity<String> deleteBoardListFromBoard(@PathVariable Long boardId,
                                                           @PathVariable Long boardListId) {
        boardService.deleteBoardListFromBoard(boardService.getBoardById(boardId),
                boardListService.getBoardListById(boardListId));
        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }
}