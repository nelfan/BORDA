package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.*;
import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.*;
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
@AllArgsConstructor
@Log
@CrossOrigin
public class BoardController {

    private final ModelMapper modelMapper;

    private final BoardService boardService;

    private final UserService userService;

    private final BoardListService boardListService;

    private final UserBoardRelationService userBoardRelationService;

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    private final JwtConvertor jwtConvertor;

    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @GetMapping("/boards/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @GetMapping("/users/{userId}/boards")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserId(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(userService.getBoardsByUserId(userId)
                    .stream().map(board -> modelMapper.map(board,
                            BoardFullDTO.class)).collect(Collectors.toList()),

                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{userId}/boards/{boardRoleId}")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserIdAndBoardRoleId(@PathVariable Long userId,
                                                                              @PathVariable Long boardRoleId) {
        try {
            return new ResponseEntity<>(
                    userService.getBoardsByUserIdAndBoardRoleId(userId, boardRoleId)
                            .stream().map(board -> modelMapper.map(board,
                            BoardFullDTO.class)).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{userId}")
    public ResponseEntity<BoardFullDTO> createBoard(@PathVariable Long userId,
                                                    @RequestBody CreateBoardDTO boardDTO) {
        try {
            Board board = new Board();
            board.setName(boardDTO.getName());

            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setBoard(board);
            userBoardRelation.setUser((userService.getUserById(userId)));

            userBoardRelation.setBoardRole(userBoardRelationService
                    .getBoardRoleByName(BoardRole.BoardRoles.OWNER.name()));

            board.setUserBoardRelations(Collections.singletonList(userBoardRelation));

            return new ResponseEntity<>(modelMapper.map(
                    boardService.createOrUpdate(board),
                    BoardFullDTO.class), HttpStatus.CREATED);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        try {
            boardService.deleteBoardById(id);
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>("Failed to delete board with Id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/boards/{id}")
    public ResponseEntity<BoardFullDTO> updateBoard(@PathVariable Long id,
                                                    @RequestBody BoardFullDTO board) {
        try {
            Board existingBoard = boardService.getBoardById(id);
            BeanUtils.copyProperties(board, existingBoard);
            return new ResponseEntity<>(modelMapper.map(
                    boardService.createOrUpdate(existingBoard),
                    BoardFullDTO.class), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<List<BoardListDTO>> getAllBoardListsForBoard(@PathVariable Long boardId) {
        try {
            return new ResponseEntity<>(
                    boardService.getAllBoardListsByBoardId(boardId)
                            .stream().map(boardList -> modelMapper.map(boardList,
                            BoardListDTO.class)).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<BoardListDTO> createBoardListsForBoard(@PathVariable Long boardId,
                                                                 @RequestBody BoardListDTO boardListDTO) {
        try {
            BoardList boardList = new BoardList();
            boardList.setName(boardListDTO.getName());
            boardList = boardListService.createOrUpdate(boardList);
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardService.addBoardListToBoard(
                                    boardService.getBoardById(boardId), boardList),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/boards/{boardId}/boardLists/{boardListId}")
    public ResponseEntity<String> deleteBoardListFromBoard(@PathVariable Long boardId,
                                                           @PathVariable Long boardListId) {
        try {
            boardService.deleteBoardListFromBoard(boardService.getBoardById(boardId),
                    boardListService.getBoardListById(boardListId));
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>("Failed to delete boardList with Id: " + boardListId,
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}")
    public ResponseEntity<BoardListDTO> getBoardListById(@PathVariable Long listId,
                                                         @PathVariable String boardId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.getBoardListById(listId),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/boards/{boardId}/lists/{listId}")
    public ResponseEntity<BoardListDTO> updateBoardList(@PathVariable Long listId,
                                                        @RequestBody BoardListDTO boardList,
                                                        @PathVariable String boardId) {
        try {
            BoardList existingBoardList = boardListService.getBoardListById(listId);
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

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForBoardList(@PathVariable Long listId,
                                                                     @PathVariable String boardId) {
        try {
            return new ResponseEntity<>(
                    boardListService.getAllTicketsByBoardListId(listId)
                            .stream().map(ticket -> modelMapper.map(ticket,
                            TicketDTO.class)).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets")
    public ResponseEntity<BoardListDTO> createTicketForBoardList(@PathVariable long listId,
                                                                 @RequestBody TicketDTO ticketDTO,
                                                                 @PathVariable String boardId) {
        try {
            Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
            ticket = ticketService.createOrUpdate(ticket);
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.addTicketToBoardList(
                                    boardListService.getBoardListById(listId), ticket),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/boards/{boardId}/lists/{listId}/tickets/{ticketId}")
    public ResponseEntity<BoardListDTO> deleteTicketFromBoardList(@PathVariable Long listId,
                                                                  @PathVariable Long ticketId,
                                                                  @PathVariable String boardId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(
                            boardListService.deleteTicketFromBoardList(
                                    boardListService.getBoardListById(listId),
                                    ticketService.getTicketById(ticketId)),
                            BoardListDTO.class),
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(modelMapper.map(
                    boardListService.getBoardListById(listId),
                    BoardListDTO.class),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{oldBoardListId}/move/{newBoardListId}/tickets/{ticketId}/")
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

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId,
                                                   @PathVariable String boardId,
                                                   @PathVariable String listId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(ticketService.getTicketById(ticketId),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/boards/{boardId}/lists/{listId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticket,
                                                  @PathVariable String boardId,
                                                  @PathVariable String listId) {
        try {
            Ticket existingTicket = ticketService.getTicketById(ticketId);
            BeanUtils.copyProperties(ticket, existingTicket);
            return new ResponseEntity<>(modelMapper.map(
                    ticketService.createOrUpdate(existingTicket),
                    TicketDTO.class), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable Long ticketId,
                                                                  @PathVariable String boardId,
                                                                  @PathVariable String listId) {
        try {
            return new ResponseEntity<>(
                    ticketService.getAllCommentsByTicketId(ticketId)
                            .stream().map(comment ->
                            modelMapper.map(comment, CommentDTO.class))
                            .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId(@PathVariable Long ticketId,
                                                          @PathVariable String boardId,
                                                          @PathVariable String listId) {
        try {
            return new ResponseEntity<>(ticketService.getAllTagsByTicketId(ticketId)
                    .stream().map(tag ->
                            modelMapper.map(tag, TagDTO.class))
                    .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId(@PathVariable Long ticketId,
                                                                    @PathVariable String boardId,
                                                                    @PathVariable String listId) {
        try {
            return new ResponseEntity<>(ticketService.getAllMembersByTicketId(ticketId)
                    .stream().map(user ->
                            modelMapper.map(user, UserSimpleDTO.class))
                    .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId(@PathVariable Long commentId,
                                                            @PathVariable String boardId,
                                                            @PathVariable String listId,
                                                            @PathVariable String ticketId) {
        try {
            return new ResponseEntity<>(modelMapper.map(
                    commentService.getCommentById(commentId),
                    CommentDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments")
    public ResponseEntity<TicketDTO> addCommentToTicketAndUser(@PathVariable Long ticketId,
                                                               @RequestBody CommentDTO commentDTO,
                                                               @PathVariable String boardId,
                                                               @PathVariable String listId) {
        try {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setUser(userService.getUserById(commentDTO.getUserId()));
            comment = commentService.createOrUpdate(comment);
            userService.addCommentToUser(comment.getUser().getId(), comment);
            return new ResponseEntity<>(
                    modelMapper.map(
                            ticketService.addCommentToTicket(ticketId, comment),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId(@PathVariable Long commentId,
                                                               @RequestBody CommentDTO commentDTO,
                                                               @PathVariable String boardId,
                                                               @PathVariable String listId,
                                                               @PathVariable String ticketId) {
        try {
            Comment existingComment = commentService.getCommentById(commentId);
            BeanUtils.copyProperties(commentDTO, existingComment);
            return new ResponseEntity<>(
                    modelMapper.map(
                            commentService.createOrUpdate(existingComment),
                            CommentDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/deleteComment/{commentId}")
    public ResponseEntity<TicketDTO> deleteCommentFromTicketAndUser(@PathVariable Long ticketId,
                                                                    @PathVariable Long commentId,
                                                                    @PathVariable String boardId,
                                                                    @PathVariable String listId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            ticketService.deleteCommentFromTicket(ticketId, comment);
            userService.deleteCommentFromUser(comment.getUser().getId(), comment);
            commentService.deleteCommentById(commentId);
            return new ResponseEntity<>(modelMapper.map(
                    ticketService.deleteCommentFromTicket(ticketId, comment),
                    TicketDTO.class),
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(modelMapper.map(
                    ticketService.getTicketById(ticketId),
                    TicketDTO.class),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId(@PathVariable Long tagId,
                                                @PathVariable String boardId,
                                                @PathVariable String listId,
                                                @PathVariable String ticketId) {
        try {
            return new ResponseEntity<>(
                    modelMapper.map(
                            tagService.getTagById(tagId),
                            TagDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/tags")
    public ResponseEntity<TicketDTO> addTagToTicket(@PathVariable Long ticketId,
                                                    @RequestBody TagDTO tagDTO,
                                                    @PathVariable String boardId,
                                                    @PathVariable String listId) {
        try {
            Tag tag = new Tag();
            tag.setText(tagDTO.getText());
            tag.setColor(tagDTO.getColor());
            tag = tagService.createOrUpdate(tag);
            return new ResponseEntity<>(
                    modelMapper.map(
                            ticketService.addTagToTicket(ticketId, tag),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boards/{boardId}/tags")
    public ResponseEntity<List<TagDTO>> getAllTagsForBoard(@PathVariable String boardId) {
        // TODO rework tags to relate to certain board and make this return all tags of one board
        try {
            return new ResponseEntity<>(tagService.getAll().stream()
                    .map(user -> modelMapper.map(user, TagDTO.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByBoardIdAndTagId(@PathVariable Long tagId,
                                                             @RequestBody TagDTO tagDTO,
                                                             @PathVariable String boardId) {
        // TODO rework tags to relate to certain board and make this update tag from certain board
        try {
            Tag existingTag = tagService.getTagById(tagId);
            BeanUtils.copyProperties(tagDTO, existingTag);
            return new ResponseEntity<>(
                    modelMapper.map(
                            tagService.createOrUpdate(existingTag),
                            TagDTO.class),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}//tickets/{ticketId}/deleteTag/{tagId}")
    public ResponseEntity<TicketDTO> deleteTagFromTicket(@PathVariable Long ticketId,
                                                         @PathVariable Long tagId,
                                                         @PathVariable String boardId,
                                                         @PathVariable String listId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            return new ResponseEntity<>(
                    modelMapper.map(
                            ticketService.deleteTagFromTicket(ticketId, tag),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(modelMapper.map(
                    ticketService.getTicketById(ticketId),
                    TicketDTO.class),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> addUserToTicket(@PathVariable Long ticketId,
                                                     @PathVariable Long userId,
                                                     @PathVariable String boardId,
                                                     @PathVariable String listId) {
        try {
            User user = userService.getUserById(userId);
            return new ResponseEntity<>(
                    modelMapper.map(
                            ticketService.addMemberToTicket(ticketId, user),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> deleteUserFromTicket(@PathVariable Long ticketId,
                                                          @PathVariable Long userId,
                                                          @PathVariable String boardId,
                                                          @PathVariable String listId) {
        try {
            User user = userService.getUserById(userId);
            return new ResponseEntity<>(
                    modelMapper.map(
                            ticketService.deleteMemberFromTicket(ticketId, user),
                            TicketDTO.class),
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(modelMapper.map(
                    ticketService.getTicketById(ticketId),
                    TicketDTO.class),
                    HttpStatus.NOT_FOUND);
        }
    }
}