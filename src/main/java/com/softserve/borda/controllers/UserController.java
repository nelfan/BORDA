package com.softserve.borda.controllers;

import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
