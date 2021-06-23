package com.softserve.borda.controllers;

import com.softserve.borda.dto.*;
import com.softserve.borda.entities.*;
import com.softserve.borda.services.*;
import com.softserve.borda.utils.CustomBeanUtils;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Log
@CrossOrigin
public class BoardController {

    private final SinkService sinkService;

    private final ModelMapper modelMapper;

    private final BoardService boardService;

    private final UserService userService;

    private final BoardColumnService boardColumnService;

    private final UserBoardRelationService userBoardRelationService;

    private final UserBoardRoleService userBoardRoleService;

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    private final InvitationService invitationService;

    @GetMapping("admin/boards")
    public List<Board> getAllBoards() {
        return boardService.getAll();
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #id)")
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
                                                    @RequestBody @Valid CreateBoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        User user = userService.getUserByUsername(authentication.getName());

        boardService.create(board);

        UserBoardRelation userBoardRelation = new UserBoardRelation();

        userBoardRelation.setUser(user);
        userBoardRelation.setBoard(board);

        userBoardRelation.setUserBoardRoleId(UserBoardRole.UserBoardRoles.OWNER.getId());

        userBoardRelationService.create(userBoardRelation);

        BoardFullDTO boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

        return new ResponseEntity<>(boardFullDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("@securityService.hasBoardManagementAccess(authentication, #id)")
    @DeleteMapping(value = "/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoardById(id);

        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardManagementAccess(authentication, #id)")
    @PutMapping(value = "/boards/{id}")
    public ResponseEntity<BoardFullDTO> updateBoard(@PathVariable Long id,
                                                    @RequestBody @Valid BoardFullDTO boardFullDTO) {
        Board board = boardService.getBoardById(id);
        CustomBeanUtils.copyNotNullProperties(boardFullDTO, board);
        board = boardService.update(board);
        boardFullDTO = modelMapper.map(board, BoardFullDTO.class);

        return new ResponseEntity<>(boardFullDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)")
    @GetMapping(value = "/boards/{boardId}/columns", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<List<BoardColumnDTO>>> getAllBoardColumnsForBoard(@PathVariable Long boardId) {
        List<BoardColumnDTO> boardColumnDTOS = getUpdatedColumns(boardId);

        Sinks.Many<List<BoardColumnDTO>> sink = sinkService.getColumnsSink(boardId);
        sink.tryEmitNext(boardColumnDTOS);

        return new ResponseEntity<>(sink.asFlux(), HttpStatus.OK);
    }

    private List<BoardColumnDTO> getUpdatedColumns(Long boardId) {
        List<BoardColumn> boardColumns = boardColumnService.getAllBoardColumnsByBoardId(boardId);

        List<BoardColumnDTO> boardColumnDTOS = boardColumns.stream()
                .map(boardColumn -> modelMapper.map(boardColumn,
                        BoardColumnDTO.class)).collect(Collectors.toList());

        return boardColumnDTOS;
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)")
    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<BoardColumnDTO> createBoardColumnsForBoard(@PathVariable Long boardId,
                                                                     @RequestBody BoardColumnDTO boardColumnDTO) {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName(boardColumnDTO.getName());
        boardColumn.setBoardId(boardId);
        boardColumn = boardColumnService.create(boardColumn);
        boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #columnId)")
    @DeleteMapping(value = "/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<String> deleteBoardColumnFromBoard(@PathVariable Long boardId,
                                                             @PathVariable Long columnId) {
        boardColumnService.deleteBoardColumnById(columnId);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #columnId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<BoardColumnDTO> getBoardColumnById(@PathVariable Long columnId,
                                                             @PathVariable String boardId) {
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BoardColumnDTO boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #columnId)")
    @PutMapping(value = "/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<BoardColumnDTO> updateBoardColumn(@PathVariable Long columnId,
                                                            @RequestBody BoardColumnDTO boardColumnDTO,
                                                            @PathVariable Long boardId) {
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        CustomBeanUtils.copyNotNullProperties(boardColumnDTO, boardColumn);
        boardColumn = boardColumnService.update(boardColumn);
        boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #columnId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets")
    public ResponseEntity<Flux<List<TicketDTO>>> getAllTicketsForBoardColumn(@PathVariable Long columnId,
                                                                       @PathVariable Long boardId) {
        List<TicketDTO> ticketDTOs = getUpdatedTickets(columnId);

        Sinks.Many<List<TicketDTO>> sink = sinkService.getTicketsSink(columnId);
        sink.tryEmitNext(ticketDTOs);

        return new ResponseEntity<>(sink.asFlux(), HttpStatus.OK);
    }

    private List<TicketDTO> getUpdatedTickets(Long columnId) {
        List<Ticket> tickets = ticketService.getAllTicketsByBoardColumnId(columnId);
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(ticket -> modelMapper.map(ticket,
                        TicketDTO.class)).collect(Collectors.toList());

        return ticketDTOs;
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #columnId)")
    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets")
    public ResponseEntity<TicketDTO> createTicketForBoardColumn(@PathVariable long columnId,
                                                                @RequestBody TicketDTO ticketDTO,
                                                                @PathVariable Long boardId) {
        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
        ticket.setBoardColumnId(columnId);
        ticket = ticketService.create(ticket);
        ticketDTO = modelMapper.map(ticket, TicketDTO.class);

//        sinkService.getTicketsSink(columnId).tryEmitNext(getUpdatedTickets(columnId));

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @DeleteMapping(value = "/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<BoardColumnDTO> deleteTicketFromBoardColumn(@PathVariable Long columnId,
                                                                      @PathVariable Long ticketId,
                                                                      @PathVariable Long boardId) {
        ticketService.deleteTicketById(ticketId);
        BoardColumn boardColumn = boardColumnService.getBoardColumnById(columnId);
        BoardColumnDTO boardColumnDTO = modelMapper.map(boardColumn, BoardColumnDTO.class);

//        sinkService.getTicketsSink(columnId).tryEmitNext(getUpdatedTickets(columnId));

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(boardColumnDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #oldBoardColumnId, #ticketId)" +
            " && @securityService.isColumnBelongsToBoard(#boardId, #newBoardColumnId)")
    @PostMapping("/boards/{boardId}/columns/{oldBoardColumnId}/move/{newBoardColumnId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> moveTicketToAnotherBoardColumn(@PathVariable Long oldBoardColumnId,
                                                                    @PathVariable Long newBoardColumnId,
                                                                    @PathVariable Long ticketId,
                                                                    @PathVariable Long boardId) {

        Ticket ticket = ticketService.moveTicketToBoardColumn(newBoardColumnId, ticketId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

//        sinkService.getTicketsSink(oldBoardColumnId).tryEmitNext(getUpdatedTickets(oldBoardColumnId));
//        sinkService.getTicketsSink(newBoardColumnId).tryEmitNext(getUpdatedTickets(newBoardColumnId));

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId,
                                                   @PathVariable Long boardId,
                                                   @PathVariable Long columnId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @PutMapping(value = "/boards/{boardId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticketDTO,
                                                  @PathVariable Long boardId,
                                                  @PathVariable Long columnId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        CustomBeanUtils.copyNotNullProperties(ticketDTO, ticket);
        ticket = ticketService.update(ticket);
        ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        List<Comment> comments = commentService.getAllCommentsByTicketId(ticketId);
        List<CommentDTO> commentDTOs = comments.stream().map(comment ->
                modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId
            (@PathVariable Long ticketId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        List<Tag> tags = tagService.getAllTagsByTicketId(ticketId);
        List<TagDTO> tagDTOs = tags.stream().map(tag ->
                modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId
            (@PathVariable Long ticketId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        List<User> users = userService.getAllMembersByTicketId(ticketId);
        List<UserSimpleDTO> userDTOs = users.stream().map(user ->
                modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isCommentBelongsToBoard(#boardId, #columnId, #ticketId, #commentId)")
    @GetMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId
            (@PathVariable Long commentId,
             @PathVariable Long boardId,
             @PathVariable Long columnId,
             @PathVariable Long ticketId) {
        Comment comment = commentService.getCommentById(commentId);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments")
    public ResponseEntity<CommentDTO> addCommentToTicket
            (@PathVariable Long ticketId,
             @RequestBody CreateCommentDTO commentDTO,
             @PathVariable Long boardId,
             @PathVariable String columnId,
             Authentication authentication) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());

        User user = userService.getUserByUsername(authentication.getName());
        comment.setUser(user);
        comment.setTicketId(ticketId);
        comment = commentService.create(comment);

        CommentDTO simpleCommentDTO = modelMapper.map(comment, CommentDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(simpleCommentDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isCommentBelongsToBoard(#boardId, #columnId, #ticketId, #commentId)")
    @PutMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId
            (@PathVariable Long commentId,
             @RequestBody CommentDTO commentDTO,
             @PathVariable Long boardId,
             @PathVariable Long columnId,
             @PathVariable Long ticketId) {
        Comment comment = commentService.getCommentById(commentId);
        CustomBeanUtils.copyNotNullProperties(commentDTO, comment);
        comment = commentService.update(comment);
        commentDTO = modelMapper.map(comment, CommentDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            "&& @securityService.isCommentBelongsToBoard(#boardId, #columnId, #ticketId, #commentId)")
    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long commentId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        commentService.deleteCommentById(commentId);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)" +
            " && @securityService.isTagBelongsToBoard(#boardId, #tagId)")
    @GetMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId
            (@PathVariable Long tagId,
             @PathVariable Long boardId) {
        Tag tag = tagService.getTagById(tagId);
        TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)")
    @PostMapping("/boards/{boardId}/tags")
    public ResponseEntity<TagDTO> addNewTagToBoard
            (@RequestBody UpdateTagDTO updateTagDTO,
             @PathVariable Long boardId) {
        try {
            Tag tag = modelMapper.map(updateTagDTO, Tag.class);
            tag.setBoardId(boardId);
            tag = tagService.create(tag);
            TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

            sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTagBelongsToBoard(#boardId, #tagId)" +
            " && @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)")
    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/tags/{tagId}")
    public ResponseEntity<String> addTagToTicket
            (@PathVariable Long ticketId,
             @PathVariable Long tagId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        boolean result = tagService.addTagToTicket(ticketId, tagId);
        if (result) {
            sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));
            return new ResponseEntity<>("Tag has been added to ticket successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to add tag to ticket. " +
                "Please make sure ticket id and tag id are correct.", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)")
    @GetMapping("/boards/{boardId}/tags")
    public ResponseEntity<List<TagDTO>> getAllTagsForBoard
            (@PathVariable Long boardId) {
        List<Tag> tags = tagService.getAllTagsByBoardId(boardId);
        List<TagDTO> tagDTOs = tags.stream().map(tag ->
                modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(tagDTOs, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            " && @securityService.isTagBelongsToBoard(#boardId, #tagId)")
    @PutMapping("/boards/{boardId}/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByTagId
            (@PathVariable Long tagId,
             @RequestBody UpdateTagDTO updateTagDTO,
             @PathVariable Long boardId) {
        Tag tag = modelMapper.map(updateTagDTO, Tag.class);
        tag.setId(tagId);
        tag.setBoardId(boardId);
        tag = tagService.update(tag);
        TagDTO tagDTO = modelMapper.map(tag, TagDTO.class);

        sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));

        return new ResponseEntity<>(tagDTO, HttpStatus.OK);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            "&& @securityService.isTagBelongsToTicket(#boardId, #columnId, #ticketId, #tagId)")
    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/tags/{tagId}")
    public ResponseEntity<String> deleteTagFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long tagId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        Tag tag = tagService.getTagById(tagId);
        boolean result = tagService.deleteTagFromTicket(ticketId, tag.getId());

        if (result) {
            sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));
            return new ResponseEntity<>("Tag has been removed from ticket successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to remove tag from ticket. " +
                "Please make sure ticket id and tag id are correct.", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            "&& @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)" +
            "&& @securityService.hasUserBoardRelation(authentication, #userId)")
    @PostMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<String> addUserToTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        boolean result = ticketService.addMemberToTicket(ticketId, userId);

        if (result) {
            sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));
            return new ResponseEntity<>("User has been added to ticket successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to add user to ticket. " +
                "Please make sure ticket id and user id are correct.", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("@securityService.hasBoardWorkAccess(authentication, #boardId)" +
            "&& @securityService.isTicketBelongsToBoard(#boardId, #columnId, #ticketId)" +
            "&& @securityService.hasUserBoardRelation(#userId, #boardId)")
    @DeleteMapping("/boards/{boardId}/columns/{columnId}/tickets/{ticketId}/members/{userId}")
    public ResponseEntity<String> deleteUserFromTicket
            (@PathVariable Long ticketId,
             @PathVariable Long userId,
             @PathVariable Long boardId,
             @PathVariable Long columnId) {
        boolean result = ticketService.deleteMemberFromTicket(ticketId, userId);

        if (result) {
            sinkService.getColumnsSink(boardId).tryEmitNext(getUpdatedColumns(boardId));
            return new ResponseEntity<>("User has been removed from ticket successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to remove user from ticket. " +
                "Please make sure ticket id and user id are correct.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/invitations")
    public ResponseEntity<List<InvitationDTO>> getAllInvitation(
            Authentication authentication) {
        var user = userService.getUserByUsername(authentication.getName());
        List<Invitation> invitations = invitationService.getAllByReceiverId(user.getId());

        List<InvitationDTO> invitationDTOs = invitations.stream().map(
                invitation -> modelMapper.map(invitation,
                        InvitationDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(invitationDTOs);
    }

    @PreAuthorize("@securityService.hasBoardInviteRights(authentication, #boardId, #userBoardRoleId)")
    @PostMapping("/users/invitations/{receiverUsername}/boards/{boardId}/roles/{userBoardRoleId}")
    public ResponseEntity<Object> createInvitation(Authentication authentication,
                                                   @PathVariable String receiverUsername,
                                                   @PathVariable Long boardId,
                                                   @PathVariable Long userBoardRoleId) {
        User sender = userService.getUserByUsername(authentication.getName());
        User receiver = userService.getUserByUsername(receiverUsername);
        if (sender.getId().equals(receiver.getId())) {
            return ResponseEntity.badRequest()
                    .body("Sender Id and Receiver Id should not be the same");
        }
        Invitation invitation = new Invitation();
        invitation.setSenderId(sender.getId());
        invitation.setSender(sender);
        invitation.setReceiverId(receiver.getId());
        invitation.setReceiver(receiver);
        invitation.setBoardId(boardId);
        invitation.setBoard(boardService.getBoardById(boardId));
        invitation.setUserBoardRoleId(userBoardRoleId);

        invitation.setUserBoardRole(userBoardRoleService.getUserBoardRoleById(userBoardRoleId));

        invitation = invitationService.create(invitation);
        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);
        return ResponseEntity.ok(invitationDTO);
    }

    @PreAuthorize("@securityService.isUserAReceiver(authentication, #invitationId)")
    @PostMapping("/users/invitations/{invitationId}")
    public ResponseEntity<Boolean> acceptOrDeclineInvitation(@PathVariable Long invitationId,
                                                             @RequestBody InvitationAcceptDTO acceptDTO) {
        Boolean isAccepted = acceptDTO.getIsAccepted();
        if (Objects.isNull(isAccepted)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAccepted) {
            return ResponseEntity.ok(invitationService.accept(invitationId));
        } else {
            return ResponseEntity.ok(invitationService.decline(invitationId));
        }
    }

    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)")
    @GetMapping("/boards/{boardId}/users")
    public ResponseEntity<List<UserSimpleDTO>> getUsersByBoardId(@PathVariable Long boardId,
                                                                 Authentication authentication) {
        List<User> users = userService.getUsersByBoardId(boardId);
        List<UserSimpleDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }


    @GetMapping("/boards/{boardId}/filteredTickets")
    public  ResponseEntity<Set<TicketDTO>> getFilteredTasks(@PathVariable Long boardId, @RequestParam Long[] tagsId, Authentication authentication){
        if(tagsId.length==0) return ResponseEntity.badRequest().body(null);
        Set<TicketDTO> ticketDTOS = ticketService.getFilteredTicketsByTags(tagsId, boardId).stream().map(ticket -> modelMapper.map(ticket,TicketDTO.class)).collect(Collectors.toSet());
        return new ResponseEntity<>(ticketDTOS, HttpStatus.OK);
    }



    @PreAuthorize("@securityService.hasUserBoardRelation(authentication, #boardId)")
    @GetMapping("/boards/{boardId}/users/roles/{roleId}")
    public ResponseEntity<List<UserSimpleDTO>> getUsersByBoardIdAndUserBoardRoleId(@PathVariable Long boardId,
                                                                                   @PathVariable Long roleId,
                                                                                   Authentication authentication) {
        List<User> users = userService.getUsersByBoardIdAndUserBoardRoleId(boardId, roleId);
        List<UserSimpleDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
}