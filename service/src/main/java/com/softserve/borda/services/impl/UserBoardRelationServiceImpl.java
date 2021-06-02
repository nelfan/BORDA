package com.softserve.borda.services.impl;

import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.entities.UserBoardRole;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.UserBoardRoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.UserBoardRelationService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class UserBoardRelationServiceImpl implements UserBoardRelationService {

    private final UserBoardRelationRepository userBoardRelationRepository;

    private final UserBoardRoleRepository userBoardRoleRepository;

    @Override
    public List<UserBoardRelation> getAll() {
        return userBoardRelationRepository.findAll();
    }

    @Override
    public UserBoardRelation getUserBoardRelationById(Long id) {
        return userBoardRelationRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRelation.class));
    }

    @Override
    public UserBoardRelation create(UserBoardRelation userBoardRelation) {
        return userBoardRelationRepository.save(userBoardRelation);
    }

    @Override
    public UserBoardRelation update(UserBoardRelation userBoardRelation) {
        UserBoardRelation existingUserBoardRelation =
                getUserBoardRelationById(userBoardRelation.getId());
        existingUserBoardRelation.setUserBoardRole(userBoardRelation.getUserBoardRole());
        return userBoardRelationRepository.save(existingUserBoardRelation);
    }

    @Override
    public boolean deleteUserBoardRelationById(Long id) {
        try {
            userBoardRelationRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public UserBoardRole getBoardRoleByName(String name) {
        return userBoardRoleRepository.findByName(name)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }

    @Override
    public UserBoardRole getBoardRoleById(Long id) {
        return userBoardRoleRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }

    @Override
    public List<UserBoardRelation> getUserBoardRelationsByUserId(Long userId) {
        return userBoardRelationRepository.findAllByUserId(userId);
    }

    @Override
    public List<UserBoardRelation> getUserBoardRelationsByUserIdAndBoardRoleId(Long userId,
                                                                               Long boardRoleId) {
        return userBoardRelationRepository.findAllByUserIdAndUserBoardRoleId(userId, boardRoleId);
    }
}
