package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.CommentDTO;
import com.softserve.borda.dto.TagDTO;
import com.softserve.borda.dto.TicketDTO;
import com.softserve.borda.dto.UserSimpleDTO;
import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.CommentService;
import com.softserve.borda.services.TagService;
import com.softserve.borda.services.TicketService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Log
@CrossOrigin
public class TicketController {

    private final ModelMapper modelMapper;

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    private final UserService userService;

    private final JwtConvertor jwtConvertor;

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId) {
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

    @PutMapping(value = "/tickets/{ticketId}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long ticketId,
                                                  @RequestBody TicketDTO ticket) {
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

    @GetMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable Long ticketId) {
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

    @GetMapping("/tickets/{ticketId}/tags")
    public ResponseEntity<List<TagDTO>> getTagsByTicketId(@PathVariable Long ticketId) {
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

    @GetMapping("/tickets/{ticketId}/members")
    public ResponseEntity<List<UserSimpleDTO>> getMembersByTicketId(@PathVariable Long ticketId) {
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

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentByCommentId(@PathVariable Long commentId) {
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

    @PostMapping("/tickets/{ticketId}/addComment")
    public ResponseEntity<TicketDTO> addCommentToTicketAndUser(@PathVariable Long ticketId,
                                                               @RequestBody CommentDTO commentDTO) {
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

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentByCommentId(@PathVariable Long commentId,
                                                               @RequestBody CommentDTO commentDTO) {
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

    @DeleteMapping("/tickets/{ticketId}/deleteComment/{commentId}")
    public ResponseEntity<TicketDTO> deleteCommentFromTicketAndUser(@PathVariable Long ticketId,
                                                                    @PathVariable Long commentId) {
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

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagDTO> getTagByTagId(@PathVariable Long tagId) {
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

    @PostMapping("/tickets/{ticketId}/addTag")
    public ResponseEntity<TicketDTO> addTagToTicket(@PathVariable Long ticketId,
                                                    @RequestBody TagDTO tagDTO) {
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

    @GetMapping("/tagList")
    public ResponseEntity<List<TagDTO>> listOfTags(){
        try {
            return new ResponseEntity<>(tagService.getAll().stream()
                    .map(user -> modelMapper.map(user, TagDTO.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<TagDTO> updateTagByTagId(@PathVariable Long tagId,
                                                   @RequestBody TagDTO tagDTO) {
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

    @DeleteMapping("/tickets/{ticketId}/deleteTag/{tagId}")
    public ResponseEntity<TicketDTO> deleteTagFromTicket(@PathVariable Long ticketId,
                                                         @PathVariable Long tagId) {
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

    @PostMapping("/tickets/{ticketId}/addMember")
    public ResponseEntity<TicketDTO> addUserToTicket(@PathVariable Long ticketId,
                                                     @RequestHeader String authorization) {
        try {
            User user = jwtConvertor.getUserByJWT(authorization);
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

    @DeleteMapping("/tickets/{ticketId}/deleteMember")
    public ResponseEntity<TicketDTO> deleteUserFromTicket(@PathVariable Long ticketId,
                                                          @RequestHeader String authorization) {
        try {
            User user = jwtConvertor.getUserByJWT(authorization);
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
