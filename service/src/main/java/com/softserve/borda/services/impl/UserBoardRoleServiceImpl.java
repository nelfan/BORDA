package com.softserve.borda.services.impl;

import com.softserve.borda.entities.UserBoardRole;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import com.softserve.borda.repositories.UserBoardRoleRepository;
import com.softserve.borda.services.UserBoardRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class UserBoardRoleServiceImpl implements UserBoardRoleService {

    private final UserBoardRoleRepository userBoardRoleRepository;

    @Override
    public List<UserBoardRole> getAll() {
        return userBoardRoleRepository.findAll();
    }

    @Override
    public UserBoardRole getUserBoardRoleById(Long id) {
        return userBoardRoleRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }

    @Override
    public UserBoardRole getUserBoardRoleByName(String name) {
        return userBoardRoleRepository.findByName(name)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }

    @Override
    public UserBoardRole create(UserBoardRole userBoardRole) {
        return userBoardRoleRepository.save(userBoardRole);
    }

    @Override
    public UserBoardRole update(UserBoardRole userBoardRole) {
        UserBoardRole existingUserBoardRole =
                getUserBoardRoleById(userBoardRole.getId());
        existingUserBoardRole.setName(userBoardRole.getName());
        return userBoardRoleRepository.save(existingUserBoardRole);
    }

    @Override
    public boolean deleteUserBoardRoleById(Long id) {
        try {
            userBoardRoleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new CustomFailedToDeleteEntityException(UserBoardRole.class);
        }
    }

    @Override
    public UserBoardRole findByUserBoardRelationId(Long userBoardRelationId) {
        return userBoardRoleRepository.findByUserBoardRelationsId(userBoardRelationId)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }
}
