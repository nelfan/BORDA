package com.softserve.borda.services;

import com.softserve.borda.entities.UserBoardRole;

public interface UserBoardRoleService {

    UserBoardRole getUserBoardRoleByName(String name);

    UserBoardRole getUserBoardRoleById(Long id);
}
