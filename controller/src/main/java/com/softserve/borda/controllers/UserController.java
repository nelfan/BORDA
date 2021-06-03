package com.softserve.borda.controllers;

import com.softserve.borda.dto.CreateUserDTO;
import com.softserve.borda.dto.UserFullDTO;
import com.softserve.borda.dto.UserSimpleDTO;
import com.softserve.borda.dto.UserUpdateDTO;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Log
@CrossOrigin
public class UserController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserSimpleDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserSimpleDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserSimpleDTO> getAuthenticatedUser(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());

        UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);
        return new ResponseEntity<>(userSimpleDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserSimpleDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user = userService.create(user);
        UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

        return new ResponseEntity<>(userSimpleDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return new ResponseEntity<>("Entity was removed successfully",
                HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserSimpleDTO> updateUser(@RequestBody UserUpdateDTO userUpdateDTO,
                                                    Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());

        BeanUtils.copyProperties(userUpdateDTO, user);
        user = userService.update(user);
        UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

        return new ResponseEntity<>(userSimpleDTO, HttpStatus.OK);
    }

}