package com.softserve.borda.controllers;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody final User user) {
        return userService.createOrUpdate(user);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping(value = "{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getUserById(id);
        BeanUtils.copyProperties(user, existingUser);
        return userService.createOrUpdate(existingUser);
    }

    @GetMapping("{id}/boards")
    public List<Board> getBoardsByUserId(@PathVariable Long id) {
        return userService.getBoardsByUserId(id);
    }

    @GetMapping("{id}/boardsByRole/{boardRoleId}")
    public List<Board> getBoardsByBoardRoleAndUserId(@PathVariable Long id,
                                               @PathVariable Long boardRoleId) {
        return userService.getBoardsByUserId(id);
    }

}
