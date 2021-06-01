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

    @GetMapping("/users/boards")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUser(@RequestHeader String authorization) {
        try {
            User user = jwtConvertor.getUserByJWT(authorization);
            List<Board> boards = boardService.getBoardsByUserId(user.getId());
            List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                    BoardFullDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/boards/{boardRoleId}")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserAndBoardRoleId(
            @RequestHeader String authorization,
            @PathVariable Long boardRoleId) {
        try {
            User user = jwtConvertor.getUserByJWT(authorization);
            List<Board> boards = boardService.getBoardsByUserIdAndBoardRoleId(user.getId(), boardRoleId);
            List<BoardFullDTO> boardDTOs = boards.stream().map(board -> modelMapper.map(board,
                    BoardFullDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards")
    public ResponseEntity<BoardFullDTO> createBoard(@RequestHeader String authorization,
                                                    @RequestBody CreateBoardDTO boardDTO) {
        try {
            Board board = new Board();
            board.setName(boardDTO.getName());

            UserBoardRelation userBoardRelation = new UserBoardRelation();
            userBoardRelation.setBoard(board);

            User user = jwtConvertor.getUserByJWT(authorization);
            userBoardRelation.setUser(user);

            userBoardRelation.setBoardRole(userBoardRelationService
                    .getBoardRoleByName(BoardRole.BoardRoles.OWNER.name()));

            board.setUserBoardRelations(Collections.singletonList(userBoardRelation));

            board = boardService.create(board);
            BoardFullDTO boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

            return new ResponseEntity<>(boardFullDTO, HttpStatus.CREATED);
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
                                                    @RequestBody BoardFullDTO boardFullDTO) {
        try {
            Board board = boardService.getBoardById(id);
            BeanUtils.copyProperties(boardFullDTO, board);
            board = boardService.update(board);
            boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

            return new ResponseEntity<>(boardFullDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<List<BoardListDTO>> getAllBoardListsForBoard(@PathVariable Long boardId) {
        try {
            List<BoardList> boardLists = boardListService.getAllBoardListsByBoardId(boardId);
            List<BoardListDTO> boardListDTOs = boardLists.stream()
                    .map(boardList -> modelMapper.map(boardList,
                            BoardListDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(boardListDTOs, HttpStatus.OK);
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
            boardList = boardListService.create(boardList);
            boardList = boardListService.addBoardListToBoard(boardId, boardList.getId());
            boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/boards/{boardId}/lists/{listId}")
    public ResponseEntity<String> deleteBoardListFromBoard(@PathVariable Long boardId,
                                                           @PathVariable Long listId) {
        try {
            boardListService.deleteBoardListFromBoard(boardId, listId);

            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>("Failed to delete boardList with Id: " + listId,
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}")
    public ResponseEntity<BoardListDTO> getBoardListById(@PathVariable Long listId,
                                                         @PathVariable String boardId) {
        try {
            BoardList boardList = boardListService.getBoardListById(listId);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/boards/{boardId}/lists/{listId}")
    public ResponseEntity<BoardListDTO> updateBoardList(@PathVariable Long listId,
                                                        @RequestBody BoardListDTO boardListDTO,
                                                        @PathVariable String boardId) {
        try {
            BoardList boardList = boardListService.getBoardListById(listId);
            BeanUtils.copyProperties(boardListDTO, boardList);
            boardList = boardListService.update(boardList);
            boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicketsForBoardList(@PathVariable Long listId,
                                                                     @PathVariable String boardId) {
        try {
            List<Ticket> tickets = ticketService.getAllTicketsByBoardListId(listId);
            List<TicketDTO> ticketDTOs = tickets.stream()
                    .map(ticket -> modelMapper.map(ticket,
                            TicketDTO.class)).collect(Collectors.toList());

            return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
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
            ticket = ticketService.create(ticket);
            ticketService.addTicketToBoardList(listId, ticket.getId());
            BoardList boardList = boardListService.getBoardListById(listId);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
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
            ticketService.deleteTicketFromBoardList(listId, ticketId);
            BoardList boardList = boardListService.getBoardListById(listId);
            BoardListDTO boardListDTO = modelMapper.map(boardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{oldBoardListId}/move/{newBoardListId}/tickets/{ticketId}/")
    public ResponseEntity<BoardListDTO> moveTicketToAnotherBoardList(@PathVariable Long oldBoardListId,
                                                                     @PathVariable Long newBoardListId,
                                                                     @PathVariable Long ticketId,
                                                                     @PathVariable String boardId) {
        try {
            BoardList oldBoardList = boardListService.getBoardListById(oldBoardListId);
            BoardList newBoardList = boardListService.getBoardListById(newBoardListId);
            Ticket ticket = ticketService.getTicketById(ticketId);
            oldBoardList.getTickets().remove(ticket);
            newBoardList.getTickets().add(ticket);
            boardListService.update(oldBoardList);
            newBoardList = boardListService.update(newBoardList);
            BoardListDTO boardListDTO = modelMapper.map(newBoardList, BoardListDTO.class);

            return new ResponseEntity<>(boardListDTO, HttpStatus.OK);
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
            Ticket ticket = ticketService.getTicketById(ticketId);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/boards/{boardId}/lists/{listId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticketDTO,
                                                  @PathVariable String boardId,
                                                  @PathVariable String listId) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            BeanUtils.copyProperties(ticketDTO, ticket);
            ticket = ticketService.update(ticket);
            ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String listId) {
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

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String listId) {
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

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId
            (@PathVariable Long ticketId,
             @PathVariable String boardId,
             @PathVariable String listId) {
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

    @GetMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId
            (@PathVariable Long commentId,
             @PathVariable String boardId,
             @PathVariable String listId,
             @PathVariable String ticketId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments")
    public ResponseEntity<TicketDTO> addCommentToTicket
            (@PathVariable Long ticketId,
             @RequestBody CommentDTO commentDTO,
             @RequestHeader String authorization,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            User user = jwtConvertor.getUserByJWT(authorization);
            comment.setUser(user);
            comment = commentService.create(comment);
            Ticket ticket = ticketService.addCommentToTicket(ticketId, comment.getId());
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId
            (@PathVariable Long commentId,
             @RequestBody CommentDTO commentDTO,
             @PathVariable String boardId,
             @PathVariable String listId,
             @PathVariable String ticketId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            BeanUtils.copyProperties(commentDTO, comment);
            comment = commentService.update(comment);
            commentDTO = modelMapper.map(comment, CommentDTO.class);

            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/deleteComment/{commentId}")
    public ResponseEntity<TicketDTO> deleteCommentFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long commentId,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            ticketService.deleteCommentFromTicket(ticketId, commentId);
            commentService.deleteCommentById(commentId);
            Ticket ticket = ticketService.deleteCommentFromTicket(ticketId, commentId);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId
            (@PathVariable Long tagId,
             @PathVariable String boardId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/tags")
    public ResponseEntity<TicketDTO> addTagToTicket
            (@PathVariable Long ticketId,
             @RequestBody TagDTO tagDTO,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            // TODO rework tags to relate to certain board
            //  and here tags should not be created, just added to ticket
            Tag tag = modelMapper.map(tagDTO, Tag.class);
            tag = tagService.create(tag);
            Ticket ticket = ticketService.addTagToTicket(ticketId, tag.getId());
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boards/{boardId}/tags")
    public ResponseEntity<List<TagDTO>> getAllTagsForBoard
            (@PathVariable String boardId) {
        try {
            // TODO rework tags to relate to certain board
            //  and make this return all tags of one board
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByTagId
            (@PathVariable Long tagId,
             @RequestBody TagDTO tagDTO,
             @PathVariable String boardId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            BeanUtils.copyProperties(tagDTO, tag);
            tag = tagService.update(tag);
            tagDTO = modelMapper.map(tag, TagDTO.class);

            return new ResponseEntity<>(tagDTO, HttpStatus.OK);

        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/deleteTag/{tagId}")
    public ResponseEntity<TicketDTO> deleteTagFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long tagId,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            Ticket ticket = ticketService.deleteTagFromTicket(ticketId, tag.getId());
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> addUserToTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            Ticket ticket = ticketService.addMemberToTicket(ticketId, userId);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/boards/{boardId}/lists/{listId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<TicketDTO> deleteUserFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable String boardId,
             @PathVariable String listId) {
        try {
            Ticket ticket = ticketService.deleteMemberFromTicket(ticketId, userId);
            TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

            return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}