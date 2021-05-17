package com.softserve.borda.services.impl;

import com.softserve.borda.entities.*;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(User.class));
    }

    @Override
    public User createOrUpdate(User user) {
        if (user.getId() != null) {
            Optional<User> userOptional = userRepository.findById(user.getId());

            if (userOptional.isPresent()) {
                User newUser = userOptional.get();
                newUser.setUsername(user.getUsername());
                newUser.setPassword(user.getPassword());
                newUser.setEmail(user.getEmail());
                newUser.setFirstName(user.getFirstName());
                newUser.setLastName(user.getLastName());
                newUser.setAvatar(user.getAvatar());
                return userRepository.save(newUser);
            }
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Board> getBoardsByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomEntityNotFoundException(User.class));
        return user.getUserBoardRelations().stream().map(
                UserBoardRelation::getBoard
        ).collect(Collectors.toList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomEntityNotFoundException(User.class));
    }

    @Override
    public User addCommentToUser(Long userId, Comment comment) {
        User user = getUserById(userId);
        user.getComments().add(comment);
        return userRepository.save(user);
    }

    @Override
    public User deleteCommentFromUser(Long userId, Comment comment) {
        User user = getUserById(userId);
        user.getComments().remove(comment);
        return userRepository.save(user);
    }
}
