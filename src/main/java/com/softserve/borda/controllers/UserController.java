package com.softserve.borda.controllers;

import com.softserve.borda.dto.GetSimpleUserDTO;
import com.softserve.borda.dto.CreateUserDTO;
import com.softserve.borda.dto.UpdateUserDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
//@SessionAttributes("user")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    private User user = null;

    @ModelAttribute("user")
    public User getUser() {
        if(userService!=null)
        user = userService.getUserById(1L);
        return user;
    }

    @GetMapping
    public List<GetSimpleUserDTO> getAllUsers() {
        return userService.getAll().stream()
                .map((user) -> modelMapper.map(user, GetSimpleUserDTO.class))
                .collect(Collectors.toList());
    }


    @GetMapping("user")
    public GetSimpleUserDTO getUser(@ModelAttribute("user") User user) {
        return modelMapper.map(
                userService.getUserById(1L),
                GetSimpleUserDTO.class);
    }

    @GetMapping("{id}")
    public GetSimpleUserDTO getUserById(@PathVariable Long id) {
        return modelMapper.map(
                userService.getUserById(id),
                GetSimpleUserDTO.class);
    }

    @PostMapping
    public GetSimpleUserDTO createUser(@RequestBody final CreateUserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.getRoles().add(new Role(Role.Roles.USER.name()));
        return modelMapper.map(
                userService.createOrUpdate(user),
                GetSimpleUserDTO.class);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping(value = "{id}")
    public GetSimpleUserDTO update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getUserById(id);
        BeanUtils.copyProperties(user, existingUser);
        return modelMapper.map(
                userService.createOrUpdate(existingUser),
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
    @GetMapping("user/boards")
    public List<Board> getBoardsByUser(@ModelAttribute("user") User user, HttpServletRequest request,
                                       SessionStatus sessionStatus) {
        return userService.getBoardsByUserId(user.getId());
    }

    @GetMapping("{id}/boardsByRole/{boardRoleId}")
    public List<Board> getBoardsByBoardRoleAndUserId(@PathVariable Long id,
                                               @PathVariable Long boardRoleId) {
        return userService.getBoardsByUserId(id); //TODO
    }

}