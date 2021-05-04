package com.softserve.borda.repositories;

import com.softserve.borda.entities.Permission;
import com.softserve.borda.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> getAllPermissionsByRolesContaining(Role role);
}
