package com.softserve.borda.controllers;

import com.softserve.borda.dto.CommentDTO;
import com.softserve.borda.dto.TagDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    private final UserService userService;

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        try {
            return new ResponseEntity<>(ticketService.getTicketById(ticketId), HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/tickets/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId) {
        try {
            ticketService.deleteTicketById(ticketId);
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.NOT_FOUND);
        } catch (CustomFailedToDeleteEntityException e) {
            return new ResponseEntity<>("Failed to delete ticket with Id: " + ticketId,
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/tickets/{ticketId}")
    public Ticket updateTicket(@PathVariable Long ticketId,
                               Ticket ticket) {
        Ticket existingTicket = ticketService.getTicketById(ticketId);
        BeanUtils.copyProperties(ticket, existingTicket);
        return ticketService.createOrUpdate(existingTicket);
    }

    @GetMapping("/tickets/{ticketId}/comments")
    public List<Comment> getCommentsByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllCommentsByTicketId(ticketId);
    }

    @GetMapping("/tickets/{ticketId}/tags")
    public List<Tag> getTagsByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllTagsByTicketId(ticketId);
    }
    
    @GetMapping("/tickets/{ticketId}/members")
    public List<User> getMembersByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllMembersByTicketId(ticketId);
    }

    @GetMapping("/comments/{commentId}")
    public Comment getCommentByCommentId(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping("/tickets/{ticketId}/addComment")
    public Ticket addCommentToTicketAndUser(@PathVariable Long ticketId,
                                            CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setUser(userService.getUserById(commentDTO.getUserId()));
        comment = commentService.createOrUpdate(comment);
        userService.addCommentToUser(comment.getUser().getId(), comment);
        return ticketService.addCommentToTicket(ticketId, comment);
    }

    @PutMapping("/comments/{commentId}")
    public Comment updateCommentByCommentId(@PathVariable Long commentId,
                                            CommentDTO commentDTO) {
        Comment existingComment = commentService.getCommentById(commentId);
        BeanUtils.copyProperties(commentDTO, existingComment);
        return commentService.createOrUpdate(existingComment);
    }

    @DeleteMapping("/tickets/{ticketId}/deleteComment/{commentId}")
    public Ticket deleteCommentFromTicketAndUser(@PathVariable Long ticketId,
                                            @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        userService.deleteCommentFromUser(comment.getUser().getId(), comment);
        commentService.deleteCommentById(commentId);
        return ticketService.deleteCommentFromTicket(ticketId, comment);
    }

    @GetMapping("/tags/{tagId}")
    public Tag getTagByTagId(@PathVariable Long tagId) {
        return tagService.getTagById(tagId);
    }

    @PostMapping("/tickets/{ticketId}/addTag")
    public Ticket addTagToTicket(@PathVariable Long ticketId,
                                            TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setText(tagDTO.getText());
        tag.setColor(tagDTO.getColor());
        tag = tagService.createOrUpdate(tag);
        return ticketService.addTagToTicket(ticketId, tag);
    }

    @PutMapping("/tags/{tagId}")
    public Tag updateTagByTagId(@PathVariable Long tagId,
                                            TagDTO tagDTO) {
        Tag existingTag = tagService.getTagById(tagId);
        BeanUtils.copyProperties(tagDTO, existingTag);
        return tagService.createOrUpdate(existingTag);
    }

    @DeleteMapping("/tickets/{ticketId}/deleteTag/{tagId}")
    public Ticket deleteTagFromTicket(@PathVariable Long ticketId,
                                                 @PathVariable Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        return ticketService.deleteTagFromTicket(ticketId, tag);
    }

    @PostMapping("/tickets/{ticketId}/addMember/{userId}")
    public Ticket addUserToTicket(@PathVariable Long ticketId,
                                  @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.addMemberToTicket(ticketId, user);
    }

    @DeleteMapping("/tickets/{ticketId}/deleteMember/{userId}")
    public Ticket deleteUserFromTicket(@PathVariable Long ticketId,
                                      @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.deleteMemberFromTicket(ticketId, user);
    }
}
