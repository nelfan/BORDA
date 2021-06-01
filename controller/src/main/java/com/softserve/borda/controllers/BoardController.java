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

    private final CommentService commentService;

    private final TagService tagService;

    private final TicketService ticketService;

    private final JwtConvertor jwtConvertor;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @GetMapping("/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @GetMapping("/{userId}/boards")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserId(@PathVariable Long userId) {
        try {
            List<Board> boards = userService.getBoardsByUserId(userId);
            List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                    BoardFullDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{userId}/boardsByRole/{boardRoleId}")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserIdAndBoardRoleId(
            @PathVariable Long userId,
            @PathVariable Long boardRoleId) {
        try {
            List<Board> boards = userService.getBoardsByUserIdAndBoardRoleId(userId, boardRoleId);
            List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                    BoardFullDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("createBoard/{userId}")
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

            board = boardService.createOrUpdate(board);
            BoardFullDTO boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

            return new ResponseEntity<>(boardFullDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{id}")
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

    @PutMapping(value = "{id}")
    public ResponseEntity<BoardFullDTO> updateBoard(@PathVariable Long id,
                                                    @RequestBody BoardFullDTO boardFullDTO) {
        try {
            Board board = boardService.getBoardById(id);
            BeanUtils.copyProperties(boardFullDTO, board);
            board = boardService.createOrUpdate(board);
            boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

            return new ResponseEntity<>(boardFullDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}/boardLists")
    public ResponseEntity<List<BoardListDTO>> getAllBoardListsForBoard(@PathVariable Long id) {
        try {
            List<BoardList> boardLists = boardService.getAllBoardListsByBoardId(id);
            List<BoardListDTO> boardListDTOs = boardLists.stream()
                    .map(boardList -> modelMapper.map(boardList,
                            BoardListDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardListDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{boardId}/addBoardList")
    public ResponseEntity<BoardListDTO> createBoardListsForBoard(@PathVariable Long boardId,
                                                                 @RequestBody BoardListDTO boardListDTO) {
        try {
            BoardList boardList = new BoardList();
            boardList.setName(boardListDTO.getName());
            boardList = boardListService.createOrUpdate(boardList);
            boardList = boardService.addBoardListToBoard(
                    boardService.getBoardById(boardId), boardList);
            boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            log.severe(e.getMessage());
            return new ResponseEntity<>("Failed to delete boardList with Id: " + boardListId,
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{boardListId}")
    public ResponseEntity<BoardListDTO> getBoardListById(@PathVariable Long boardListId) {
        try {
            BoardList boardList = boardListService.getBoardListById(boardListId);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{boardListId}")
    public ResponseEntity<BoardListDTO> updateBoardList(@PathVariable Long boardListId,
                                                        @RequestBody BoardListDTO boardListDTO) {
        try {
            BoardList boardList = boardListService.getBoardListById(boardListId);
            BeanUtils.copyProperties(boardListDTO, boardList);
            boardList = boardListService.createOrUpdate(boardList);
            boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{boardListId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForBoardList(@PathVariable Long boardListId) {
        try {
            List<Ticket> tickets = boardListService.getAllTicketsByBoardListId(boardListId);
            List<TicketDTO> ticketDTOs = tickets.stream()
                    .map(ticket -> modelMapper.map(ticket,
                            TicketDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
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
            BoardList boardList = boardListService.getBoardListById(boardListId);
            boardList = boardListService.addTicketToBoardList(boardList, ticket);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{boardListId}/tickets/{ticketId}")
    public ResponseEntity<BoardListDTO> deleteTicketFromBoardList(@PathVariable Long boardListId,
                                                                  @PathVariable Long ticketId) {
        try {
            BoardList boardList = boardListService.getBoardListById(boardListId);
            Ticket ticket = ticketService.getTicketById(ticketId);
            boardList = boardListService.deleteTicketFromBoardList(boardList,
                    ticket);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            newBoardList = boardListService.createOrUpdate(newBoardList);
            BoardListDTO boardListDTO = modelMapper.map(newBoardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            BeanUtils.copyProperties(ticketDTO, ticket);
            ticket = ticketService.createOrUpdate(ticket);
            ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable Long ticketId) {
        try {
            List<Comment> comments = ticketService.getAllCommentsByTicketId(ticketId);
            List<CommentDTO> commentDTOs = comments.stream().map(comment ->
                    modelMapper.map(comment, CommentDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId(@PathVariable Long ticketId) {
        try {
            List<Tag> tags = ticketService.getAllTagsByTicketId(ticketId);
            List<TagDTO> tagDTOs = tags.stream().map(tag ->
                    modelMapper.map(tag, TagDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId(@PathVariable Long ticketId) {
        try {
            List<User> users = ticketService.getAllMembersByTicketId(ticketId);
            List<UserSimpleDTO> userDTOs = users.stream().map(user ->
                    modelMapper.map(user, UserSimpleDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId(@PathVariable Long commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tickets/{ticketId}/addComment")
    public ResponseEntity<TicketDTO> addCommentToTicketAndUser(@PathVariable Long ticketId,
                                                               @RequestBody CommentDTO commentDTO) {
        try {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setUser(userService.getUserById(commentDTO.getUserId()));
            comment = commentService.createOrUpdate(comment);
            userService.addCommentToUser(comment.getUser().getId(), comment);
            Ticket ticket = ticketService.addCommentToTicket(ticketId, comment);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId(@PathVariable Long commentId,
                                                               @RequestBody CommentDTO commentDTO) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            BeanUtils.copyProperties(commentDTO, comment);
            comment = commentService.createOrUpdate(comment);
            commentDTO = modelMapper.map(comment, CommentDTO.class);

            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tickets/{ticketId}/deleteComment/{commentId}")
    public ResponseEntity<TicketDTO> deleteCommentFromTicketAndUser(@PathVariable Long ticketId,
                                                                    @PathVariable Long commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            ticketService.deleteCommentFromTicket(ticketId, comment);
            userService.deleteCommentFromUser(comment.getUser().getId(), comment);
            commentService.deleteCommentById(commentId);
            Ticket ticket = ticketService.deleteCommentFromTicket(ticketId, comment);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId(@PathVariable Long tagId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tickets/{ticketId}/addTag")
    public ResponseEntity<TicketDTO> addTagToTicket(@PathVariable Long ticketId,
                                                    @RequestBody TagDTO tagDTO) {
        try {
            Tag tag = modelMapper.map(tagDTO, Tag.class);
            tag = tagService.createOrUpdate(tag);
            Ticket ticket = ticketService.addTagToTicket(ticketId, tag);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tagList")
    public ResponseEntity<List<TagDTO>> listOfTags() {
        try {
            List<Tag> tags = tagService.getAll();
            List<TagDTO> tagDTOs = tags.stream()
                    .map(user -> modelMapper.map(user, TagDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByTagId(@PathVariable Long tagId,
                                                   @RequestBody TagDTO tagDTO) {
        try {
            Tag tag = tagService.getTagById(tagId);
            BeanUtils.copyProperties(tagDTO, tag);
            tag = tagService.createOrUpdate(tag);
            tagDTO = modelMapper.map(tag, TagDTO.class);

            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tickets/{ticketId}/deleteTag/{tagId}")
    public ResponseEntity<TicketDTO> deleteTagFromTicket(@PathVariable Long ticketId,
                                                         @PathVariable Long tagId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            Ticket ticket = ticketService.deleteTagFromTicket(ticketId, tag);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tickets/{ticketId}/addMember/{userId}")
    public ResponseEntity<TicketDTO> addUserToTicket(@PathVariable Long ticketId,
                                                     @PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            Ticket ticket = ticketService.addMemberToTicket(ticketId, user);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tickets/{ticketId}/deleteMember/{userId}")
    public ResponseEntity<TicketDTO> deleteUserFromTicket(@PathVariable Long ticketId,
                                                          @PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            Ticket ticket = ticketService.deleteMemberFromTicket(ticketId, user);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}