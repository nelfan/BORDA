package com.softserve.borda.services.impl;

import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User createOrUpdate(User user) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
