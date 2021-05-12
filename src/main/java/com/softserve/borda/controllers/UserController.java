package com.softserve.borda.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import com.softserve.borda.services.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
//@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService userService;

    private User user = null;


    @JsonIgnore
    @ModelAttribute("user")
    public User getUser(){
        if(userService!=null) user = userService.getUserById(1L);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }


    @GetMapping("user")
    public User getUser(@ModelAttribute("user") User user, HttpServletRequest request,
                        SessionStatus sessionStatus) {
        return user;
    }


    @PostMapping
    public User createUser(@RequestBody final User user) {
        System.out.println(user);
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
    @GetMapping("user/boards")
    public List<Board> getBoardsByUserId(@ModelAttribute("user") User user, HttpServletRequest request,
                                         SessionStatus sessionStatus) {
        return userService.getBoardsByUserId(user.getId());
    }

    @GetMapping("{id}/boardsByRole/{boardRoleId}")
    public List<Board> getBoardsByBoardRoleAndUserId(@PathVariable Long id,
                                               @PathVariable Long boardRoleId) {
        return userService.getBoardsByUserId(id);
    }

}
