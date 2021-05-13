package com.softserve.borda.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.borda.dto.CreateUserDTO;
import com.softserve.borda.dto.GetSimpleUserDTO;
import com.softserve.borda.dto.UpdateUserDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;

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
    public GetSimpleUserDTO getUser(@ModelAttribute("user") User user) {
        return modelMapper.map(
                user,
                GetSimpleUserDTO.class);
    }

    @PostMapping("update")
    public GetSimpleUserDTO updateUser(@RequestBody final UpdateUserDTO userDTO) {
        modelMapper.map(userDTO, user);
        userService.createOrUpdate(user);
        return modelMapper.map(
                userService.createOrUpdate(user),
                GetSimpleUserDTO.class);
    }


    @PostMapping
    public GetSimpleUserDTO createUser(@RequestBody final CreateUserDTO userDTO) {
        user = modelMapper.map(userDTO, User.class);
        user.getRoles().add(new Role(Role.Roles.USER.name()));
        userService.createOrUpdate(user);
        return modelMapper.map(
                userService.createOrUpdate(user),
                GetSimpleUserDTO.class);
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

    @GetMapping("user/boards")
    public List<Board> getBoardsByUserId(@ModelAttribute("user") User user) {
        return userService.getBoardsByUserId(user.getId());
    }

    @GetMapping("{id}/boardsByRole/{boardRoleId}")
    public List<Board> getBoardsByBoardRoleAndUserId(@PathVariable Long id,
                                                     @PathVariable Long boardRoleId) {
        return userService.getBoardsByUserId(id);
    }

}
