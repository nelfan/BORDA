package com.softserve.borda.services.impl;

import com.softserve.borda.entities.UserBoardRole;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.UserBoardRoleRepository;
import com.softserve.borda.services.UserBoardRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserBoardRoleServiceImpl implements UserBoardRoleService {

    private final UserBoardRoleRepository userBoardRoleRepository;
    @Override
    public UserBoardRole getUserBoardRoleByName(String name) {
        return userBoardRoleRepository.findByName(name)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }

    @Override
    public UserBoardRole getUserBoardRoleById(Long id) {
        return userBoardRoleRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(UserBoardRole.class));
    }
}
