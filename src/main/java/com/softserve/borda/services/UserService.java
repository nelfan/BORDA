package com.softserve.borda.services;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    User createOrUpdate(User user);
    void deleteUserById(Long id);

    List<Board> getBoardsByUserId(Long id);

    User getUserByUsername(String username);

    User findByLoginAndPassword(String login, String password);
}
