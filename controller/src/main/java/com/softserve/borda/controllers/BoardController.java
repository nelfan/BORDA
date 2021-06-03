package com.softserve.borda.controllers;

import com.softserve.borda.dto.*;
import com.softserve.borda.entities.*;
import com.softserve.borda.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Log
@CrossOrigin
public class BoardController {

    private final ModelMapper modelMapper;

    private final BoardService boardService;

    private final UserService userService;

    private final BoardColumnService boardColumnService;

    private final UserBoardRelationService userBoardRelationService;

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @GetMapping("/boards/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @GetMapping("/users/boards")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUser(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        List<Board> boards = boardService.getBoardsByUserId(user.getId());
        List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                BoardFullDTO.class)).collect(Collectors.toList());

        return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/boards/{boardRoleId}")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserAndBoardRoleId(Authentication authentication,
                                                                            @PathVariable Long boardRoleId) {
        User user = userService.getUserByUsername(authentication.getName());
        List<Board> boards = boardService.getBoardsByUserIdAndBoardRoleId(user.getId(), boardRoleId);
        List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                BoardFullDTO.class)).collect(Collectors.toList());

        return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
    }

    @PostMapping("/boards")
    public ResponseEntity<BoardFullDTO> createBoard(Authentication authentication,
                                                    @RequestBody CreateBoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        UserBoardRelation userBoardRelation = new UserBoardRelation();
        userBoardRelation.setBoard(board);

        User user = userService.getUserByUsername(authentication.getName());
        userBoardRelation.setUser(user);

        userBoardRelation.setUserBoardRole(userBoardRelationService
                .getUserBoardRoleByName(UserBoardRole.BoardRoles.OWNER.name()));

        board.setUserBoardRelations(Collections.singletonList(userBoardRelation));

        board = boardService.create(board);
        BoardFullDTO boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

        return new ResponseEntity<>(boardFullDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoardById(id);

        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @PutMapping(value = "/boards/{id}")
    public ResponseEntity<BoardFullDTO> updateBoard(@PathVariable Long id,
                                                    @RequestBody BoardFullDTO boardFullDTO) {
        Board board = boardService.getBoardById(id);
        BeanUtils.copyProperties(boardFullDTO, board);
        board = boardService.update(board);
        boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

        return new ResponseEntity<>(boardFullDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<List<BoardColumnDTO>> getAllBoardColumnsForBoard(@PathVariable Long boardId) {
        List<BoardColumn> boardColumns = boardColumnService.getAllBoardColumnsByBoardId(boardId);
        List<BoardColumnDTO> boardColumnDTOS = boardColumns.stream()
                .map(boardColumn -> modelMapper.map(boardColumn,
                        BoardColumnDTO.class)).collect(Collectors.toList());

        return new ResponseEntity<>(boardColumnDTOS, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<BoardColumnDTO> createBoardColumnsForBoard(@PathVariable Long boardId,
                                                                   @RequestBody BoardColumnDTO boardColumnDTO) {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName(boardColumnDTO.getName());
        boardColumn = boardColumnService.create(boardColumn);
        boardColumn = boardColumnService.addBoardColumnToBoard(boardId, boardColumn.getId());
        boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<String> deleteBoardColumnFromBoard(@PathVariable Long boardId,
                                                           @PathVariable Long columnId) {
        boardColumnService.deleteBoardColumnFromBoard(boardId, columnId);

        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<BoardColumnDTO> getBoardColumnById(@PathVariable Long columnId,
                                                           @PathVariable String boardId) {
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BoardColumnDTO boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<BoardColumnDTO> updateBoardColumn(@PathVariable Long columnId,
                                                          @RequestBody BoardColumnDTO boardColumnDTO,
                                                          @PathVariable String boardId) {
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BeanUtils.copyProperties(boardColumnDTO, boardColumn);
        boardColumn = boardColumnService.update(boardColumn);
        boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForBoardColumn(@PathVariable Long columnId,
                                                                     @PathVariable String boardId) {
        List<Ticket> tickets = ticketService.getAllTicketsByBoardColumnId(columnId);
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(ticket -> modelMapper.map(ticket,
                        TicketDTO.class)).collect(Collectors.toList());

        return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets")
    public ResponseEntity<BoardColumnDTO> createTicketForBoardColumn(@PathVariable long columnId,
                                                                   @RequestBody TicketDTO ticketDTO,
                                                                   @PathVariable String boardId) {
        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
        ticket = ticketService.create(ticket);
        ticketService.addTicketToBoardColumn(columnId, ticket.getId());
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BoardColumnDTO boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<BoardColumnDTO> deleteTicketFromBoardColumn(@PathVariable Long columnId,
                                                                    @PathVariable Long ticketId,
                                                                    @PathVariable String boardId) {
        ticketService.deleteTicketFromBoardColumn(columnId, ticketId);
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BoardColumnDTO boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/{oldBoardColumnId}/move/{newBoardColumnId}/tickets/{ticketId}/")
    public ResponseEntity<BoardColumnDTO> moveTicketToAnotherBoardColumn(@PathVariable Long oldBoardColumnId,
                                                                       @PathVariable Long newBoardColumnId,
                                                                       @PathVariable Long ticketId,
                                                                       @PathVariable String boardId) {
        BoardColumn oldBoardColumn = boardColumnService.getBoardColumnById(oldBoardColumnId);
        BoardColumn newBoardColumn = boardColumnService.getBoardColumnById(newBoardColumnId);
        Ticket ticket = ticketService.getTicketById(ticketId);
        oldBoardColumn.getTickets().remove(ticket);
        newBoardColumn.getTickets().add(ticket);
        boardColumnService.update(oldBoardColumn);
        newBoardColumn = boardColumnService.update(newBoardColumn);
        BoardColumnDTO boardColumnDTO = modelMapper.map(newBoardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId,
                                                   @PathVariable String boardId,
                                                   @PathVariable String columnId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticketDTO,
                                                  @PathVariable String boardId,
                                                  @PathVariable String columnId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        BeanUtils.copyProperties(ticketDTO, ticket);
        ticket = ticketService.update(ticket);
        ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        List<Comment> comments = ticketService.getAllCommentsByTicketId(ticketId);
        List<CommentDTO> commentDTOs = comments.stream().map(comment ->
                modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        List<Tag> tags = ticketService.getAllTagsByTicketId(ticketId);
        List<TagDTO> tagDTOs = tags.stream().map(tag ->
                modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        List<User> users = ticketService.getAllMembersByTicketId(ticketId);
        List<UserSimpleDTO> userDTOs = users.stream().map(user ->
                modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId
            (@PathVariable Long commentId,
             @PathVariable String boardId,
             @PathVariable String columnId,
             @PathVariable String ticketId) {
        Comment comment = commentService.getCommentById(commentId);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments")
    public ResponseEntity<TicketDTO> addCommentToTicket
            (@PathVariable Long ticketId,
             @RequestBody CommentDTO commentDTO,
             @PathVariable String boardId,
             @PathVariable String columnId,
             Authentication authentication) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());

        User user = userService.getUserByUsername(authentication.getName());
        comment.setUser(user);

        comment = commentService.create(comment);

        Ticket ticket = ticketService.addCommentToTicket(ticketId, comment.getId());
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PutMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId
            (@PathVariable Long commentId,
             @RequestBody CommentDTO commentDTO,
             @PathVariable String boardId,
             @PathVariable String columnId,
             @PathVariable String ticketId) {
        Comment comment = commentService.getCommentById(commentId);
        BeanUtils.copyProperties(commentDTO, comment);
        comment = commentService.update(comment);
        commentDTO = modelMapper.map(comment, CommentDTO.class);

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/deleteComment/{commentId}")
    public ResponseEntity<TicketDTO> deleteCommentFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long commentId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        ticketService.deleteCommentFromTicket(ticketId, commentId);
        commentService.deleteCommentById(commentId);
        Ticket ticket = ticketService.deleteCommentFromTicket(ticketId, commentId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId
            (@PathVariable Long tagId,
             @PathVariable String boardId) {
        Tag tag = tagService.getTagById(tagId);
        TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/tags")
    public ResponseEntity<TicketDTO> addTagToTicket
            (@PathVariable Long ticketId,
             @RequestBody TagDTO tagDTO,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        // TODO rework tags to relate to certain board
        //  and here tags should not be created, just added to ticket
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        tag = tagService.create(tag);
        Ticket ticket = ticketService.addTagToTicket(ticketId, tag.getId());
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/tags")
    public ResponseEntity<List<TagDTO>> getAllTagsForBoard
            (@PathVariable String boardId) {
        // TODO rework tags to relate to certain board
        //  and make this return all tags of one board
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByTagId
            (@PathVariable Long tagId,
             @RequestBody TagDTO tagDTO,
             @PathVariable String boardId) {
        Tag tag = tagService.getTagById(tagId);
        BeanUtils.copyProperties(tagDTO, tag);
        tag = tagService.update(tag);
        tagDTO = modelMapper.map(tag, TagDTO.class);

        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/deleteTag/{tagId}")
    public ResponseEntity<TicketDTO> deleteTagFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long tagId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        Tag tag = tagService.getTagById(tagId);
        Ticket ticket = ticketService.deleteTagFromTicket(ticketId, tag.getId());
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> addUserToTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        Ticket ticket = ticketService.addMemberToTicket(ticketId, userId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> deleteUserFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable String boardId,
             @PathVariable String columnId) {
        Ticket ticket = ticketService.deleteMemberFromTicket(ticketId, userId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }
}