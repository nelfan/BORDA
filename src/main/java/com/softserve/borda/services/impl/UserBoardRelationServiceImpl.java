package com.softserve.borda.services.impl;

import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.BoardRoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.UserBoardRelationService;
import org.springframework.stereotype.Service;

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
    public BoardRole getBoardRoleByName(String name) {
        return boardRoleRepository.findByName(name)
                .orElseThrow(() -> new CustomEntityNotFoundException(BoardRole.class));
    }

}
