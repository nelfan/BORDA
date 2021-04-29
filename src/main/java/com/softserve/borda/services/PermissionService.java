package com.softserve.borda.services;

import com.softserve.borda.entities.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAll();
    Permission getPermissionById(Long id);
    Permission createOrUpdate(Permission permission);
    void deletePermissionById(Long id);
}

