package com.softserve.borda.services;

import com.softserve.borda.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUserById(Long id);

    User create(User user);

    User update(User user);

    boolean deleteUserById(Long id);

    User getUserByUsername(String username);

    Boolean existsUserByUsername(String username);
}
