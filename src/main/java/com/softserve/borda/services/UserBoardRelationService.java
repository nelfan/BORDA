package com.softserve.borda.services;

import com.softserve.borda.entities.Permission;
import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.UserBoardRelation;

import java.util.List;

public interface UserBoardRelationService {
    List<UserBoardRelation> getAll();
    UserBoardRelation getUserBoardRelationById(Long id);
    UserBoardRelation createOrUpdate(UserBoardRelation userBoardRelation);
    void deleteUserBoardRelationById(Long id);

    List<Role> getAllRolesByUserBoardRelationId(Long userBoardRelationId);

    List<Permission> getAllPermissionsByRoleId(Long roleId);

    List<Permission> getAllPermissionsByUserBoardRelationId(Long userBoardRelationId);
}
