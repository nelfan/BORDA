package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Permission;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;
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
        Optional<UserBoardRelation> userBoardRelation = userBoardRelationRepository.findById(id);
        if(userBoardRelation.isPresent()) {
            return userBoardRelation.get();
        }
        return null; // TODO: Throw custom exception
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
        Optional<UserBoardRelation> userBoardRelationOptional =
                userBoardRelationRepository.findById(userBoardRelation.getId());
        if(userBoardRelationOptional.isPresent()) {
            return userBoardRelationOptional.get().getBoardRoles();
        }
        return List.of(); // TODO Throw a custom error?
    }
    
    @Override
    public List<Permission> getAllPermissionsByRole(BoardRole boardRole) {
        Optional<BoardRole> roleOptional =
                boardRoleRepository.findById(boardRole.getId());
        if(roleOptional.isPresent()) {
            return roleOptional.get().getPermissions();
        }
        return List.of(); // TODO Throw a custom error?
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
        //TODO: throe custom exception
        return boardRoleRepository.findByName(name).orElse(null);
    }

}
