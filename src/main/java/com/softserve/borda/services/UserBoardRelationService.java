package com.softserve.borda.services;

import com.softserve.borda.entities.Permission;
import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;

import java.util.List;

public interface UserBoardRelationService {
    List<UserBoardRelation> getAll();
    UserBoardRelation getUserBoardRelationById(Long id);
    UserBoardRelation createOrUpdate(UserBoardRelation userBoardRelation);
    void deleteUserBoardRelationById(Long id);

    List<BoardRole> getAllRolesByUserBoardRelation(UserBoardRelation userBoardRelation);

    List<Permission> getAllPermissionsByRole(BoardRole boardRole);

    List<Permission> getAllPermissionsByUserBoardRelation(UserBoardRelation userBoardRelation);

    BoardRole getBoardRoleByName(String name);
}
