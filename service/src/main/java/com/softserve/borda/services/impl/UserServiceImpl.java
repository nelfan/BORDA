package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.RoleRepository;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.UserBoardRelationService;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserBoardRelationService userBoardRelationService;

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
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User existingUser = getUserById(user.getId());

        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAvatar(user.getAvatar());

        if (user.getRoles().isEmpty()) {
            existingUser.getRoles().add(
                    roleRepository.findByName(Role.Roles.ROLE_USER.name()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public boolean deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(User.class);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomEntityNotFoundException(User.class));
    }

    @Override
    public List<User> getUsersByUserBoardRelations(List<UserBoardRelation> userBoardRelations) {
        return userRepository.findAllByUserBoardRelationsIn(userBoardRelations);
    }

    @Override
    public List<User> getUsersByBoardId(Long boardId) {
        List<UserBoardRelation> userBoardRelations =
                userBoardRelationService.getUserBoardRelationsByBoardId(boardId);
        return userRepository.findAllByUserBoardRelationsIn(userBoardRelations);
    }

    @Override
    public List<User> getUsersByBoardIdAndUserBoardRoleId(Long boardId, Long userBoardRoleId) {
        List<UserBoardRelation> userBoardRelations =
                userBoardRelationService.getUserBoardRelationsByBoardIdAndUserBoardRoleId(boardId, userBoardRoleId);
        return userRepository.findAllByUserBoardRelationsIn(userBoardRelations);
    }

    @Override
    public Boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }
}
