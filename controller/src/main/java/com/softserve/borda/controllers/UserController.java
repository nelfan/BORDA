package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.*;
import com.softserve.borda.entities.User;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final JwtConvertor jwtConvertor;

    @GetMapping
    public ResponseEntity<List<UserSimpleDTO>> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.getAll().stream()
                    .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<UserSimpleDTO> getUserById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(modelMapper.map(
                    userService.getUserById(id)
                    ,
                    UserSimpleDTO.class), HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserSimpleDTO> getUserById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(modelMapper.map(
                    jwtConvertor.getUserByJWT(id),
                    UserSimpleDTO.class), HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<UserSimpleDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        try {
            User user = modelMapper.map(userDTO, User.class);
            return new ResponseEntity<>(modelMapper.map(
                    userService.createOrUpdate(user),
                    UserSimpleDTO.class), HttpStatus.CREATED);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>("Entity was removed successfully",
                    HttpStatus.OK);
        } catch (CustomFailedToDeleteEntityException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>("Failed to delete user with Id: " + id,
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<UserSimpleDTO> updateUser(@PathVariable Long id,
                                                    @RequestBody UserFullDTO user) {
        try {
            User existingUser = userService.getUserById(id);
            BeanUtils.copyProperties(user, existingUser);
            return new ResponseEntity<>(modelMapper.map(
                    userService.createOrUpdate(existingUser),
                    UserSimpleDTO.class), HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("update/{id}")
    public UserSimpleDTO updateUser(@RequestBody final UserUpdateDTO userDTO, @PathVariable String id) {
        var user = jwtConvertor.getUserByJWT(id);
        modelMapper.map(userDTO, user);
        return modelMapper.map(
                userService.createOrUpdate(user),
                UserSimpleDTO.class);
    }

    @GetMapping("/{userId}/boards")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserId(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(userService.getBoardsByUserId(jwtConvertor.getUserByJWT(userId).getId())
                    .stream().map(board -> modelMapper.map(board,
                            BoardFullDTO.class)).collect(Collectors.toList()),

                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{userId}/boardsByRole/{boardRoleId}")
    public ResponseEntity<List<BoardFullDTO>> getBoardsByUserIdAndBoardRoleId(@PathVariable Long userId,
                                                                              @PathVariable Long boardRoleId) {
        try {
            return new ResponseEntity<>(
                    userService.getBoardsByUserIdAndBoardRoleId(userId, boardRoleId)
                            .stream().map(board -> modelMapper.map(board,
                            BoardFullDTO.class)).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(
                    userService.getAllCommentsByUserId(userId)
                            .stream().map(comment ->
                            modelMapper.map(comment, CommentDTO.class))
                            .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}