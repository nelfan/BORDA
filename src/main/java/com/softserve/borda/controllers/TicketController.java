package com.softserve.borda.controllers;

import com.softserve.borda.dto.CommentDTO;
import com.softserve.borda.dto.TagDTO;
import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.Ticket;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.CommentService;
import com.softserve.borda.services.TagService;
import com.softserve.borda.services.TicketService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    private final CommentService commentService;

    private final TagService tagService;

    private final UserService userService;


    @GetMapping("{ticketId}/comments")
    public List<Comment> getCommentsByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllCommentsByTicketId(ticketId);
    }

    @GetMapping("{ticketId}/tags")
    public List<Tag> getTagsByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllTagsByTicketId(ticketId);
    }
    
    @GetMapping("{ticketId}/members")
    public List<User> getMembersByTicketId(@PathVariable Long ticketId) {
        return ticketService.getAllMembersByTicketId(ticketId);
    }

    @GetMapping("{commentId}")
    public Comment getCommentByCommentId(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping("{ticketId}/addComment")
    public Ticket addCommentToTicketAndUser(@PathVariable Long ticketId,
                                            @RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setUser(userService.getUserById(commentDTO.getUser().getId()));
        comment = commentService.createOrUpdate(comment);
        userService.addCommentToUser(comment.getUser().getId(), comment);
        return ticketService.addCommentToTicket(ticketId, comment);
    }

    @PutMapping("{commentId}")
    public Comment updateCommentByCommentId(@PathVariable Long commentId,
                                            @RequestBody CommentDTO commentDTO) {
        Comment existingComment = commentService.getCommentById(commentId);
        BeanUtils.copyProperties(commentDTO, existingComment);
        return commentService.createOrUpdate(existingComment);
    }

    @DeleteMapping("{ticketId}/deleteComment/{commentId}")
    public Ticket deleteCommentFromTicketAndUser(@PathVariable Long ticketId,
                                            @PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        userService.deleteCommentFromUser(comment.getUser().getId(), comment);
        commentService.deleteCommentById(commentId);
        return ticketService.deleteCommentFromTicket(ticketId, comment);
    }

    @GetMapping("{tagId}")
    public Tag getTagByTagId(@PathVariable Long tagId) {
        return tagService.getTagById(tagId);
    }

    @PostMapping("{ticketId}/addTag")
    public Ticket addTagToTicket(@PathVariable Long ticketId,
                                            @RequestBody TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setText(tagDTO.getText());
        tag.setColor(tagDTO.getColor());
        tag = tagService.createOrUpdate(tag);
        return ticketService.addTagToTicket(ticketId, tag);
    }

    @PutMapping("{tagId}")
    public Tag updateTagByTagId(@PathVariable Long tagId,
                                            @RequestBody TagDTO tagDTO) {
        Tag existingTag = tagService.getTagById(tagId);
        BeanUtils.copyProperties(tagDTO, existingTag);
        return tagService.createOrUpdate(existingTag);
    }

    @DeleteMapping("{ticketId}/deleteTag/{tagId}")
    public Ticket deleteTagFromTicket(@PathVariable Long ticketId,
                                                 @PathVariable Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        tagService.deleteTagById(tagId);
        return ticketService.deleteTagFromTicket(ticketId, tag);
    }

    @PostMapping("{ticketId}/addMember/{userId}")
    public Ticket addUserToTicket(@PathVariable Long ticketId,
                                  @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.addMemberToTicket(ticketId, user);
    }

    @DeleteMapping("{ticketId}/deleteUser/{userId}")
    public Ticket deleteUserFromTicket(@PathVariable Long ticketId,
                                      @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ticketService.deleteMemberFromTicket(ticketId, user);
    }
}
