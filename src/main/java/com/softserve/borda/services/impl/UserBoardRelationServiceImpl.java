package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Permission;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardRoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.UserBoardRelationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserBoardRelationServiceImpl implements UserBoardRelationService {
    
    private final UserBoardRelationRepository userBoardRelationRepository;

    private final BoardRoleRepository boardRoleRepository;

    public UserBoardRelationServiceImpl(UserBoardRelationRepository userBoardRelationRepository, BoardRoleRepository boardRoleRepository) {
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.boardRoleRepository = boardRoleRepository;
    }

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
    public UserBoardRelation createOrUpdate(UserBoardRelation userBoardRelation) {
        if (userBoardRelation.getId() != null) {
            Optional<UserBoardRelation> userBoardRelationOptional = userBoardRelationRepository.findById(userBoardRelation.getId());

            if (userBoardRelationOptional.isPresent()) {
                UserBoardRelation newUserBoardRelation = userBoardRelationOptional.get();
                return userBoardRelationRepository.save(newUserBoardRelation);
            }
        }
        return userBoardRelationRepository.save(userBoardRelation);
    }

    @Override
    public void deleteUserBoardRelationById(Long id) {
        userBoardRelationRepository.deleteById(id);
    }

    @Override
    public List<BoardRole> getAllRolesByUserBoardRelation(UserBoardRelation userBoardRelation) {
        return userBoardRelationRepository.findById(userBoardRelation.getId())
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRelation.class))
                .getBoardRoles();
    }
    
    @Override
    public List<Permission> getAllPermissionsByRole(BoardRole boardRole) {
        return boardRoleRepository.findById(boardRole.getId())
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardRole.class))
                .getPermissions();
    }

    @Override
    public List<Permission> getAllPermissionsByUserBoardRelation(UserBoardRelation userBoardRelation) {
        List<BoardRole> boardRoles = getAllRolesByUserBoardRelation(userBoardRelation);
        List<Permission> permissions = new ArrayList<>();
        boardRoles.forEach(role -> permissions.addAll(getAllPermissionsByRole(role)));
        return permissions;
    }

    @Override
    public BoardRole getBoardRoleByName(String name) {
        return boardRoleRepository.findByName(name)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardRole.class));
    }

}
