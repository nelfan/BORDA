package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardRole;
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
class BoardRoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void shouldInsertAndReturnRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        BoardRole expected = roleRepository.save(boardRole);
        BoardRole actual = roleRepository.getOne(boardRole.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldInsertAndDeleteRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        roleRepository.save(boardRole);
        roleRepository.deleteById(boardRole.getId());
        Assertions.assertFalse(roleRepository.findById(boardRole.getId()).isPresent());
    }

    @Test
    void shouldInsertAndUpdateRole() {
        BoardRole boardRole = new BoardRole();
        boardRole.setName("Role1");
        roleRepository.save(boardRole);
        boardRole.setName("Role1updated");
        BoardRole expected = roleRepository.save(boardRole);
        BoardRole actual = roleRepository.getOne(boardRole.getId());
        Assertions.assertEquals(expected, actual);
    }
}