package com.softserve.borda.services;

import com.softserve.borda.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role getRoleById(Long id);
    Role createOrUpdate(Role role);
    void deleteRoleById(Long id);
}
