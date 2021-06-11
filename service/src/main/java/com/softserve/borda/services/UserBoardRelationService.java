package com.softserve.borda.services;

import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.entities.UserBoardRole;

import java.util.List;

public interface UserBoardRelationService {
    List<UserBoardRelation> getAll();

    UserBoardRelation getUserBoardRelationById(Long id);

    UserBoardRelation create(UserBoardRelation userBoardRelation);

    UserBoardRelation update(UserBoardRelation userBoardRelation);

    boolean deleteUserBoardRelationById(Long id);

    List<UserBoardRelation> getUserBoardRelationsByUserId(Long userId);

    List<UserBoardRelation> getUserBoardRelationsByUserIdAndUserBoardRoleId(Long userId,
                                                                            Long userBoardRoleId);

    List<UserBoardRelation> getUserBoardRelationsByBoardId(Long boardId);

    List<UserBoardRelation> getUserBoardRelationsByBoardIdAndUserBoardRoleId(Long boardId,
                                                                             Long userBoardRoleId);

    UserBoardRelation getUserBoardRelationByUserIdAndBoardId(Long userId,
                                                             Long boardId);
}
