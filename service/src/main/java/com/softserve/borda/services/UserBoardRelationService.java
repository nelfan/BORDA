package com.softserve.borda.services;

import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.UserBoardRelation;

import java.util.List;

public interface UserBoardRelationService {
    List<UserBoardRelation> getAll();
    UserBoardRelation getUserBoardRelationById(Long id);
    UserBoardRelation create(UserBoardRelation userBoardRelation);
    UserBoardRelation update(UserBoardRelation userBoardRelation);
    boolean deleteUserBoardRelationById(Long id);

    BoardRole getBoardRoleByName(String name);

    BoardRole getBoardRoleById(Long id);

    List<UserBoardRelation> getUserBoardRelationsByUserId(Long userId);

    List<UserBoardRelation> getUserBoardRelationsByUserIdAndBoardRoleId(Long userId,
                                                                        Long boardRoleId);
}
