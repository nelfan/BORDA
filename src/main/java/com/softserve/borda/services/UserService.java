package com.softserve.borda.services;

import com.softserve.borda.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(Long id);
    User createOrUpdate(User user);
    void deleteUserById(Long id);
}
