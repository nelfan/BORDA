package com.softserve.borda.services;

import com.softserve.borda.entities.UserBoardRelation;

import java.util.List;

public interface UserBoardRelationService {
    List<UserBoardRelation> getAll();
    UserBoardRelation getUserBoardRelationById(Long id);
    UserBoardRelation createOrUpdate(UserBoardRelation userBoardRelation);
    void deleteUserBoardRelationById(Long id);
}
