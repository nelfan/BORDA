package com.softserve.borda.services;

import com.softserve.borda.entities.UserBoardRole;

import java.util.List;

public interface UserBoardRoleService {

    UserBoardRole getUserBoardRoleByName(String name);

    List<UserBoardRole> getAll();

    UserBoardRole getUserBoardRoleById(Long id);

    UserBoardRole create(UserBoardRole userBoardRole);

    UserBoardRole update(UserBoardRole userBoardRole);
}
