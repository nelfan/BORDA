package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.CreateUserDTO;
import com.softserve.borda.dto.UserFullDTO;
import com.softserve.borda.dto.UserSimpleDTO;
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
            List<User> users = userService.getAll();
            List<UserSimpleDTO> userDTOs = users.stream()
                    .map(user -> modelMapper.map(user, UserSimpleDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<UserSimpleDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

            return new ResponseEntity<>(userSimpleDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserSimpleDTO> getUserById(@PathVariable String id) {
        try {
            User user = jwtConvertor.getUserByJWT(id);
            UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

            return new ResponseEntity<>(userSimpleDTO, HttpStatus.OK);
        } catch (CustomEntityNotFoundException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<UserSimpleDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        try {
            User user = modelMapper.map(userDTO, User.class);
            user = userService.createOrUpdate(user);
            UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

            return new ResponseEntity<>(userSimpleDTO, HttpStatus.CREATED);
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
                                                    @RequestBody UserFullDTO userFullDTO) {
        try {
            User user = userService.getUserById(id);
            BeanUtils.copyProperties(userFullDTO, user);
            user = userService.createOrUpdate(user);
            UserSimpleDTO userSimpleDTO = modelMapper.map(user, UserSimpleDTO.class);

            return new ResponseEntity<>(userSimpleDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}