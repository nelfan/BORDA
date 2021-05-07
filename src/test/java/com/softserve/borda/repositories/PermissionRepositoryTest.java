package com.softserve.borda.repositories;

import com.softserve.borda.entities.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PermissionRepositoryTest {

    @Autowired
    PermissionRepository permissionRepository;

    @Test
    void shouldInsertAndReturnPermission() {
        Permission permission = new Permission();
        permission.setName("Permission1");
        Permission expected = permissionRepository.save(permission);
        Permission actual = permissionRepository.getOne(permission.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeletePermission() {
        Permission permission = new Permission();
        permission.setName("Permission1");
        permissionRepository.save(permission);
        permissionRepository.deleteById(permission.getId());
        Assertions.assertFalse(permissionRepository.findById(permission.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdatePermission() {
        Permission permission = new Permission();
        permission.setName("Permission1");
        permissionRepository.save(permission);
        permission.setName("Permission1updated");
        Permission expected = permissionRepository.save(permission);
        Permission actual = permissionRepository.getOne(permission.getId());
        Assertions.assertEquals(expected, actual);
    }
}
