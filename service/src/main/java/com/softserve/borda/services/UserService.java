package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    User createOrUpdate(User user);
    void deleteUserById(Long id);

    List<Board> getBoardsByUser(User user);

    List<Board> getBoardsByUserAndBoardRoleId(User user, Long roleId);

    User getUserByUsername(String username);

    User addCommentToUser(Long userId, Comment comment);

    User deleteCommentFromUser(Long userId, Comment comment);

    List<Comment> getAllCommentsByUser(User user);
}
