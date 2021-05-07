package com.softserve.borda.repositories;

import com.softserve.borda.entities.Role;
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
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void shouldInsertAndReturnRole() {
        Role role = new Role();
        role.setName("Role1");
        Role expected = roleRepository.save(role);
        Role actual = roleRepository.getOne(role.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteRole() {
        Role role = new Role();
        role.setName("Role1");
        roleRepository.save(role);
        roleRepository.deleteById(role.getId());
        Assertions.assertFalse(roleRepository.findById(role.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateRole() {
        Role role = new Role();
        role.setName("Role1");
        roleRepository.save(role);
        role.setName("Role1updated");
        Role expected = roleRepository.save(role);
        Role actual = roleRepository.getOne(role.getId());
        Assertions.assertEquals(expected, actual);
    }
}